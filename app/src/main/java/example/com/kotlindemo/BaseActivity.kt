package example.com.kotlindemo

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import example.com.kotlindemo.databinding.ActivityBaseBinding

/**
 * Created by zhanghongqiang on 2017/5/22.
 */
abstract class BaseActivity : AppCompatActivity() {

    lateinit var mBaseBinding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        mBaseBinding = DataBindingUtil.setContentView(this as Activity, R.layout.activity_base);
        mBaseBinding.setTitle(initTitle());
        mBaseBinding.contentView.addView(initContentView());
        mBaseBinding.toolbarBack.setOnClickListener({
            finish()

        });
    }

    fun addRightTitleView(viewDataBinding: ViewDataBinding) {
        mBaseBinding.addView.addView(viewDataBinding.getRoot());
    }

    //子类activity实现title
    abstract fun initTitle(): String

    //子类activity实现ne内容页面
    abstract fun initContentView(): View


    fun getContext(): Context {
        return this as Context
    }

}