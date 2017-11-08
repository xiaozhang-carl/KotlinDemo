package example.com.kotlindemo.binder

import android.databinding.BindingAdapter
import android.widget.ImageView
import example.com.kotlindemo.utils.ImageLoader.show


/**
 * Created by yuyakaido on 2016/12/03.
 * ToDo:自定义绑定者
 */
object CustomBinder {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun imageUrl(imageView: ImageView, url: String) {
        show(imageView,url)
    }
}