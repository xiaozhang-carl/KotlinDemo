package example.com.kotlindemo.binder

import android.databinding.BindingAdapter
import android.widget.ImageView
import example.com.kotlindemo.R
import example.com.kotlindemo.utils.ImageLoader.show
import java.util.*


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

    @JvmStatic
    @BindingAdapter("teacherImageUrl")
    fun teacherImageUrl(imageView: ImageView, url: String) {
        val ints = intArrayOf(R.mipmap.ic_teacher_bear
                ,R.mipmap.ic_teacher_fox
                ,R.mipmap.ic_teacher_rabbit
                ,R.mipmap.ic_teacher_raccoon)
        val random = Random().nextInt(4)
        show(imageView,url, ints[random])
    }
}