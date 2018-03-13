package example.com.kotlindemo.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*

/**
 * Created by zhanghongqiang on 2018/3/10 上午11:25
 * ToDo:
 */

abstract class BaseAdapter<T>(private val context: Context) : RecyclerView.Adapter<BaseAdapter.XZViewHolder>() {

    //存放数据源的数据集合
    var mDatas: ArrayList<T> = ArrayList()


    override fun getItemCount(): Int {
        return if (mDatas != null) mDatas!!.size else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XZViewHolder {
        //调用接口的方法
        var binding: ViewDataBinding = createView(parent, viewType)
        var viewHolder = XZViewHolder(binding)
        return viewHolder
    }

    override fun onBindViewHolder(holder: XZViewHolder, position: Int) {
        setData(getItem(position), holder.itemDataBinding, position)
    }

    //根据索引，找到对应的数据
    private fun getItem(position: Int): T {
        return mDatas!![position]
    }

    //清空数据集合
    fun clearList() {
        //防止刷新闪烁的出现
        val itemCount = mDatas!!.size
        mDatas!!.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    /**
     * 插入t到指定位置
     * @param position
     * @param obj
     */
    fun notifyItemRangeInserted(position: Int, t: T) {
        if (position < 0) {
            return
        }
        mDatas.add(position, t)
        notifyItemRangeInserted(position, 1)
    }

    fun addNewList(insertPosition: Int, list: List<T>?) {
        //防止刷新闪烁的出现
        if (list != null && list.size > 0) {
            mDatas!!.addAll(list)
            notifyItemRangeInserted(insertPosition, list.size)
        }
    }

    //填充xml
    fun creatViewDataBinding(xmlId: Int): ViewDataBinding {
        return DataBindingUtil.inflate(LayoutInflater.from(context), xmlId, null, false)
    }

    protected abstract fun createView(parent: ViewGroup, viewType: Int): ViewDataBinding

    protected abstract fun setData(data: T, viewDataBinding: ViewDataBinding, position: Int)

    //viewhodler
    class XZViewHolder(var itemDataBinding: ViewDataBinding) : RecyclerView.ViewHolder(itemDataBinding.root)
}
