package example.com.kotlindemo.ui

import android.content.Context
import android.widget.Toast
import example.com.kotlindemo.net.LoadingDialog
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by zhanghongqiang on 2017/5/22.
 */
abstract class NetActivity: AppActivity(), LoadingDialog{

    var mDisposales: CompositeDisposable

    init {
        mDisposales = CompositeDisposable()
    }

    override fun showLoadingDialog() {
        Toast.makeText(this as Context, "数据加载中", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoadingDialog() {
        Toast.makeText(this as Context, "数据加载Ok", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        mDisposales.clear()
    }
}