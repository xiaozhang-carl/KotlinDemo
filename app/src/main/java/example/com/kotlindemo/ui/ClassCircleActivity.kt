package example.com.kotlindemo.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.gson.GsonBuilder
import com.xiaozhang.sr.RecyclerViewContract
import com.xiaozhang.sr.RecyclerViewScrollListener
import com.xiaozhang.sr.SwipeRecyclerViewDelegate
import example.com.kotlindemo.R
import example.com.kotlindemo.databinding.ContentClassCircleBinding
import example.com.kotlindemo.databinding.ItemPictureBinding
import example.com.kotlindemo.databinding.ItemVideoBinding
import example.com.kotlindemo.model.ClassCircle
import example.com.kotlindemo.model.Content
import example.com.kotlindemo.net.Client
import example.com.kotlindemo.net.Client.dongDianUrl
import example.com.kotlindemo.rx.rx2
import example.com.kotlindemo.ui.photoview.ImageViewPagerActivity
import example.com.kotlindemo.ui.photoview.ImageViewPagerActivity.Companion.EXTRA_KEY_INDEX
import example.com.kotlindemo.ui.photoview.ImageViewPagerActivity.Companion.EXTRA_KEY_URLS
import example.com.kotlindemo.utils.ImageLoader
import io.reactivex.functions.Consumer

class ClassCircleActivity : StateFulActivity(), RecyclerViewContract.IFTypeAdapter<ClassCircle.ClassTalkRecords>, RecyclerViewContract.IFLoadData, RecyclerViewContract.IFAdapter<ClassCircle.ClassTalkRecords> {


    lateinit var mBinding: ContentClassCircleBinding

    //Gson
    private var gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()

    lateinit var mSwipeRecyclerViewDelegate: SwipeRecyclerViewDelegate<ClassCircle.ClassTalkRecords>

    override fun initTitle(): String {
        return "班级圈"
    }

    override fun initContentView(): View {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this as Activity), R.layout.content_class_circle, null, false)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.backToTop.setOnClickListener {
            mBinding.recyclerView.scrollToPosition(0)
        }

        mSwipeRecyclerViewDelegate = SwipeRecyclerViewDelegate.with(this, this)
                .recyclerView(mBinding.swipeRefreshLayout, mBinding.recyclerView)
                .setScrollListener(object : RecyclerViewScrollListener(mBinding.swipeRefreshLayout, this) {
                    override fun onScrolled(firstVisibleItemPosition: Int, lastVisibleItemPosition: Int) {
                        mBinding.backToTop.setVisibility(if (firstVisibleItemPosition == 0) View.GONE else View.VISIBLE)
                    }
                })
                .build()
                as SwipeRecyclerViewDelegate<ClassCircle.ClassTalkRecords>

        mSwipeRecyclerViewDelegate.reLoadData()

    }


    override fun loadData() {

        mDisposales.run {
            mSwipeRecyclerViewDelegate.nextPage()
            var start = mSwipeRecyclerViewDelegate.dataList.size;
            var limit = 10;

            //首页的话，start==0
            if (mSwipeRecyclerViewDelegate.page < 2) {
                start = 0
            }
            add(rx2(Client.dongDian.microSchool(
                    start, limit
            )
                    , object : Consumer<ClassCircle> {
                override fun accept(t: ClassCircle?) {
                    mSwipeRecyclerViewDelegate.render(t?.classTalkRecords)
                }
            }
                    , object : Consumer<Throwable> {
                override fun accept(t: Throwable?) {
                    mSwipeRecyclerViewDelegate.onError()
                }
            }))
        }
    }

    override fun getItemViewType(position: Int): Int {
        var data = mSwipeRecyclerViewDelegate.dataList.get(position)
        var content = gson.fromJson(data.content, Content::class.java)
        if (content.pic_urls != null) {
            return 0;
        } else {
            return 1;
        }
    }

    override fun updateView(data: ClassCircle.ClassTalkRecords, binding: ViewDataBinding, position: Int) {

        var content = gson.fromJson(data.content, Content::class.java)

        if (binding is ItemPictureBinding) {
            binding.content = content
            binding.teacher?.record = data

            var list: ArrayList<String> = ArrayList()
            var images: ArrayList<ImageView> = ArrayList()
            content.pic_urls?.forEach {
                list.add(it?.image as String)
            }

            images.add(binding.image0)
            images.add(binding.image1)
            images.add(binding.image2)
            images.add(binding.image3)
            images.add(binding.image4)
            images.add(binding.image5)
            images.add(binding.image6)
            images.add(binding.image7)
            images.add(binding.image8)

            //首先清空图片的点击事件
            images.forEach {
                it.visibility = View.INVISIBLE
                it.setOnClickListener {
                }
            }


            content.pic_urls?.forEachIndexed { index, picUrls ->

                //显示图片
                images[index].visibility = View.VISIBLE
                ImageLoader.show(images[index], picUrls.thumbnail_pic as String)
                //图片的点击事件
                images[index].setOnClickListener {
                    zoom(list, index)
                }
            }

            binding.ll1.visibility = View.GONE
            binding.ll2.visibility = View.GONE
            binding.ll3.visibility = View.GONE
            //布局的现实与影藏
            if (content.pic_urls?.size as Int >= 1)
                binding.ll1.visibility = View.VISIBLE

            if (content.pic_urls?.size as Int >= 4)
                binding.ll2.visibility = View.VISIBLE

            if (content.pic_urls?.size as Int >= 7)
                binding.ll3.visibility = View.VISIBLE

        } else {
            var contentBinding = binding as ItemVideoBinding
            contentBinding.content = content
            contentBinding.teacher?.record = data

            contentBinding.videoWeb.setOnClickListener {
                //http://weixiaologin.dongdianyun.com:9001/MicroSchool/talkRecord_getTalkRecordUrl.action?talkRecordId=101175
                var url = "${dongDianUrl}MicroSchool/talkRecord_getTalkRecordUrl.action?talkRecordId=${data.talkRecordId?.toString()}"
                var intent: Intent = Intent()
                intent.setAction(Intent.ACTION_VIEW);
                var content_url: Uri = Uri.parse(url);
                intent.setData(content_url);
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
            }
        }

    }

    fun zoom(list: ArrayList<String>, index: Int) {
        val intent: Intent = Intent(this, ImageViewPagerActivity::class.java)
        intent.putStringArrayListExtra(EXTRA_KEY_URLS, list)
        intent.putExtra(EXTRA_KEY_INDEX, index)
        startActivity(intent)
    }

    override fun createView(parent: ViewGroup?, type: Int): ViewDataBinding {
        if (type == 0) {
            return DataBindingUtil.inflate(LayoutInflater.from(this as Context), R.layout.item_picture, null, false);
        } else {
            return DataBindingUtil.inflate(LayoutInflater.from(this as Context), R.layout.item_video, null, false);
        }
    }

}