package example.com.kotlindemo.ui

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.View
import example.com.kotlindemo.R
import example.com.kotlindemo.databinding.ActivityBaseBinding

/**
 * Created by zhanghongqiang on 2017/5/22.
 */
abstract class BaseActivity : AppActivity() {

    lateinit var mBaseBinding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        mBaseBinding = DataBindingUtil.setContentView(this as Activity, R.layout.activity_base);
        mBaseBinding.setTitle(initTitle());
        mBaseBinding.contentView.addView(initContentView());
        mBaseBinding.toolbarBack.setOnClickListener({
            clickToolbarBack()
        })
    }

    //标题头部返回按键的点击结束
    protected fun clickToolbarBack() {
        finish()
    }

    fun addRightTitleView(viewDataBinding: ViewDataBinding) {
        mBaseBinding.addView.addView(viewDataBinding.getRoot());
    }

    //子类activity实现title
    abstract fun initTitle(): String

    //子类activity实现ne内容页面
    abstract fun initContentView(): View

}




