package example.com.kotlindemo.ui

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xiaozhang.sr.RecyclerViewContract
import com.xiaozhang.sr.SwipeRecyclerViewDelegate
import example.com.kotlindemo.Extension.toast
import example.com.kotlindemo.R
import example.com.kotlindemo.databinding.ContentMainBinding
import example.com.kotlindemo.databinding.ItemUserBinding
import example.com.kotlindemo.model.User
import example.com.kotlindemo.net.Client.gitHubService
import example.com.kotlindemo.rx.RxBus
import example.com.kotlindemo.rx.rx2
import io.reactivex.functions.Consumer
import android.widget.Toast as t


class MainActivity : StateFulActivity(), RecyclerViewContract.IFLoadData, RecyclerViewContract.IFAdapter<User> {


    lateinit var mBinding: ContentMainBinding

    lateinit var mSwipeRecyclerViewDelegate: SwipeRecyclerViewDelegate<User>

    override fun initTitle(): String {
        return "测试一下"
    }

    override fun initContentView(): View {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this as Activity), R.layout.content_main, null, false)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSwipeRecyclerViewDelegate = SwipeRecyclerViewDelegate
                .with(this, this)
                .recyclerView(mBinding.swipeRefreshLayout, mBinding.recyclerView)
                .build()
                as SwipeRecyclerViewDelegate<User>

        mSwipeRecyclerViewDelegate.reLoadData()

        initEventBus()
    }

    private fun initEventBus() {

        RxBus.register(User::class.java, object : Consumer<User> {
            override fun accept(u: User?) {
//                t.makeText(this@MainActivity as Context,u?.avatar_url, android.widget.Toast.LENGTH_SHORT).show()

                toast(u?.avatar_url!!);
            }
        })
    }


    override fun loadData() {
//        mDisposales.run {
//            add(rx(gitHubService.stargazers(), object : HandleNetData<List<User>>() {
//                override fun onNext(t: List<User>) {
//                    mSwipeRecyclerViewDelegate.render(t)
//                }
//
//                override fun misMatch(t: List<User>) {
//                    mSwipeRecyclerViewDelegate.onError()
//                }
//
//                override fun onError(t: Throwable?) {
//                    mSwipeRecyclerViewDelegate.onError()
//                }
//            }))
//
//        }

        mDisposales.run {
            add(rx2(gitHubService.stargazers()
                    , object : Consumer<List<User>> {
                override fun accept(t: List<User>?) {
                    mSwipeRecyclerViewDelegate.render(t)
                    }
                }
                    , object : Consumer<Throwable> {
                override fun accept(t: Throwable?) {
                    mSwipeRecyclerViewDelegate.onError()
                }
            }))
        }


//        gitHubService
//                .stargazers()
//                .compose(Composers.composeWithoutResponse())
//                .subscribe({
//                    mSwipeRecyclerViewDelegate.render(it)
//                },{
//                    mSwipeRecyclerViewDelegate.onError()
//                })
    }


    override fun updateView(data: User, binding: ViewDataBinding, position: Int) {

        var userBinding: ItemUserBinding = binding as ItemUserBinding;

        userBinding.user = data

        userBinding.root.setOnClickListener { RxBus.post(data) }

    }

    override fun createView(parent: ViewGroup?, type: Int): ViewDataBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(this as Context), R.layout.item_user, null, false);
    }

}
