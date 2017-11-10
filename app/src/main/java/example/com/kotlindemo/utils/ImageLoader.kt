package example.com.kotlindemo.utils

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import example.com.kotlindemo.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DefaultSubscriber

/**
 * Created by zhanghongqiang on 2017/11/7 下午4:19
 * ToDo:
 */
object ImageLoader {


    /**
     * 显示网络图片
     *
     * @param imageView
     * @param path
     */

    fun show(imageView: ImageView, path: String) {
        show(imageView, path, R.mipmap.img_default_honghong)

    }


    /**
     * 显示网络图片
     *
     * @param imageView
     * @param path
     * @param placeholderDrawable
     */

    fun show(imageView: ImageView, path: String, placeholderDrawable: Int) {
        var path = path
        if (path == null) {
            imageView.setImageResource(placeholderDrawable)
            return
        }
        if (!path.startsWith("http") && !path.startsWith("file")) {
            path = "file://" + path
        }

        if (path.endsWith(".gif")) {
            Glide.with(imageView.context)
                    .asGif()
                    .apply(RequestOptions.placeholderOf(placeholderDrawable)
                            .error(placeholderDrawable)
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .centerCrop())
                    .load(path)
                    .into(imageView)
        } else {
            Glide.with(imageView.context)
                    .asBitmap()
                    .apply(RequestOptions.placeholderOf(placeholderDrawable).error(placeholderDrawable).centerCrop())
                    .load(path)
                    .into(imageView)
        }
    }


    fun displayBitmap(imageView: ImageView, path: String, placeholderDrawable: Int) {
        imageView.setImageResource(placeholderDrawable)
        //得到的图片链接为空的话
        Flowable.just(path)
                .map { s ->
                    //获取资源bitmap
                    Glide.with(imageView.context).asBitmap().load(s).submit().get()
                }
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DefaultSubscriber<Bitmap>() {
                    override fun onNext(bitmap: Bitmap?) {
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap)
                        }
                    }

                    override fun onError(t: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })
    }


}