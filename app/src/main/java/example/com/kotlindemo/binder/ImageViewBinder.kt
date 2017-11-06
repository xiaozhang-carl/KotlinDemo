package com.yuyakaido.android.flow.presentation.binder

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * Created by yuyakaido on 2016/12/03.
 * ToDo:自定义绑定者
 */
object CustomBinder {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun imageUrl(imageView: ImageView, url: String?) {
        Glide.with(imageView.context).load(url).into(imageView)
    }
}