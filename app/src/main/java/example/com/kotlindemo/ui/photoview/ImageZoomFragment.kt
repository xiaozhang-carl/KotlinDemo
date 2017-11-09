package example.com.kotlindemo.ui.photoview

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import example.com.kotlindemo.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DefaultSubscriber
import uk.co.senab.photoview.PhotoViewAttacher


/**
 * Created by zhanghongqiang on 16/3/24  上午10:06
 * ToDo:
 */
class ImageZoomFragment : Fragment() {


    private var imageUrl: String? = null

    private var imageView: ImageView? = null

    private var mAttacher: PhotoViewAttacher? = null

    //5.0以上的动画
    private var animator: Animator? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        imageUrl = arguments.getString(EXTRA_KEY_IMAGE)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_image_zoom, container, false)
        imageView = view.findViewById<View>(R.id.imageView) as ImageView
        if (imageUrl == null) {
            //            ToastUtil.show("图片资源错误!");
            return view
        }
        //如果用  binding.setUrl(imageUrl); 图片的android:scaleType="centerCrop" 不会起作用
        if (imageUrl!!.startsWith("htt")) {
            //得到的图片链接为空的话
            Flowable.just(imageUrl!!)
                    .map { s ->
                        //获取资源bitmap
                        Glide.with(imageView!!.context).asBitmap().load(s).submit().get()
                    }
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DefaultSubscriber<Bitmap>() {
                        override fun onNext(bitmap: Bitmap?) {
                            if (bitmap != null) {
                                imageView!!.setImageBitmap(bitmap)
                                mAttacher = PhotoViewAttacher(imageView!!)
                            }
                        }

                        override fun onError(t: Throwable) {

                        }

                        override fun onComplete() {

                        }
                    })
        } else {

        }


        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //6.0以上的图片动画
        if (Build.VERSION.SDK_INT > 21) {
            val layoutView = view?.findViewById<View>(R.id.frameLayout) as FrameLayout
            layoutView.post {
                // 圆形动画的x坐标  位于View的中心
                val cx = (layoutView.left + layoutView.right) / 2

                //圆形动画的y坐标  位于View的中心
                val cy = (layoutView.top + layoutView.bottom) / 2

                //起始大小半径
                val startX = 0f

                //结束大小半径 大小为图片对角线的一半
                val startY = Math.sqrt((cx * cx + cy * cy).toDouble()).toFloat()

                if (Build.VERSION.SDK_INT > 21) {
                    animator = ViewAnimationUtils.createCircularReveal(layoutView, cx, cy, startX, startY)
                    //在动画开始的地方速率改变比较慢,然后开始加速
                    animator!!.interpolator = AccelerateInterpolator()
                    animator!!.duration = 600
                    animator!!.start()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (animator != null) {
            animator!!.cancel()
        }

    }

    companion object {

        private val EXTRA_KEY_IMAGE = "EXTRA_KEY_IMAGE"

        fun newInstance(imageUrl: String?): ImageZoomFragment {

            val fragment = ImageZoomFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KEY_IMAGE, imageUrl)
            fragment.arguments = bundle
            return fragment
        }
    }
}
