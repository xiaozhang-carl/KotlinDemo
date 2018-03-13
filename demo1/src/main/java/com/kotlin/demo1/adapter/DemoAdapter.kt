package example.com.kotlindemo.adapter

import android.content.Context
import android.databinding.ViewDataBinding
import android.view.ViewGroup
import com.kotlin.demo1.R
import com.kotlin.demo1.databinding.ItemPictureBinding


/**
 * Created by zhanghongqiang on 2018/3/12 上午11:40
 * ToDo:
 */
class DemoAdapter( context: Context) : BaseAdapter<String>(context) {

    override fun createView(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return creatViewDataBinding(R.layout.item_picture)
    }

    override fun setData(data: String, viewDataBinding: ViewDataBinding, position: Int) {
        if (viewDataBinding  is ItemPictureBinding){
            viewDataBinding.text.text=data
        }
    }

}