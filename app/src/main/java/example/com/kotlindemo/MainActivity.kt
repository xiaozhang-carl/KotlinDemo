package example.com.kotlindemo

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xiaozhang.sr.RecyclerViewContract
import com.xiaozhang.sr.SwipeRecyclerViewDelegate
import example.com.kotlindemo.databinding.ContentMainBinding
import example.com.kotlindemo.databinding.ItemUserBinding
import example.com.kotlindemo.event.RxBus
import example.com.kotlindemo.net.Client.gitHubService
import example.com.kotlindemo.net.HandleNetData
import example.com.kotlindemo.net.rx
import io.reactivex.functions.Consumer


class MainActivity : NetActivity(), RecyclerViewContract.IFLoadData, RecyclerViewContract.IFAdapter<User> {


    lateinit var mBinding: ContentMainBinding

    lateinit var mSwipeRecyclerViewDelegate: SwipeRecyclerViewDelegate<User>

    override fun initTitle(): String {
        return "test"
    }

    override fun initContentView(): View {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this as Context), R.layout.content_main, null, false)
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
            override fun accept(t: User?) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun loadData() {
        mDisposales.add(rx(gitHubService.stargazers(), object : HandleNetData<List<User>> {

            override fun onNext(it: List<User>) {
                mSwipeRecyclerViewDelegate.render(it)

            }

            override fun misMatch(t: List<User>) {
                mSwipeRecyclerViewDelegate.onError()
            }

            override fun onError(t: Throwable?) {
                mSwipeRecyclerViewDelegate.onError()
            }

        }))

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
