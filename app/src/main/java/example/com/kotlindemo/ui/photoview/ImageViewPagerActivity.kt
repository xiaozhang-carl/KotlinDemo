package example.com.kotlindemo.ui.photoview


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.MotionEvent
import android.view.View
import example.com.kotlindemo.R
import example.com.kotlindemo.adapter.ViewPagerAdapter
import example.com.kotlindemo.databinding.ActivityImageViewpagerBinding
import example.com.kotlindemo.ui.NetActivity
import java.util.*


/**
 * Created by zhanghongqiang on 16/3/24  上午9:56
 * ToDo:图片放大的操作,后续加入下载
 */
class ImageViewPagerActivity : NetActivity() {

    private var mBinding: ActivityImageViewpagerBinding? = null

    //图片
    private var urls: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        QMUIStatusBarHelper.translucent(this); // 沉浸式状态栏
        //        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_viewpager)
        initViewPager()
    }


    private fun initViewPager() {
        //图片数据
        urls = intent.getStringArrayListExtra(EXTRA_KEY_URLS)
        //显示那一页
        var index = intent.getIntExtra(EXTRA_KEY_INDEX, 0)
        if (index == -1) {
            index = 0
        }

        if (urls == null || urls!!.size == 0) {
            return
        }
        mBinding?.size?.text = urls?.size.toString()
        val fragments = ArrayList<Fragment>()
        for (url in urls!!) {
            fragments.add(ImageZoomFragment.newInstance(url))
        }
        mBinding!!.viewPager.adapter = ViewPagerAdapter(fragments, supportFragmentManager)
        //设置缓存
        mBinding!!.viewPager.offscreenPageLimit = urls!!.size
        mBinding!!.viewPager.currentItem = index
        mBinding!!.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mBinding?.index?.text = (position+1).toString()
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    //http://stackoverflow.com/questions/6919292/pointerindex-out-of-range-android-multitouch
    //http://stackoverflow.com/questions/16459196/java-lang-illegalargumentexception-pointerindex-out-of-range-exception-dispat#
    override fun dispatchTouchEvent(mv: MotionEvent): Boolean {
        try {
            return window.superDispatchTouchEvent(mv)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }

        return false
    }

    //点击返回按钮
    fun back(view: View) {
        finish()
    }

    companion object {

        var EXTRA_KEY_URLS = "EXTRA_KEY_URLS"
        var EXTRA_KEY_INDEX = "EXTRA_KEY_INDEX"
    }

}
