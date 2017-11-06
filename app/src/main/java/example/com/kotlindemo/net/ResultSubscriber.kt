package example.com.kotlindemo.net

import android.widget.Toast
import example.com.kotlindemo.App


/**
 * Created by zhanghongqiang on 2017/5/20.
 */
/**
 *
 */
class ResultSubscriber<T>(private val hND: HandleNetData<T>
                          , private val lD: LoadingDialog
                          , private val showToast: Boolean = true)
    : io.reactivex.subscribers.ResourceSubscriber<T>() {

    override fun onStart() {
        super.onStart()
        request(1)
        lD.showLoadingDialog()
    }

    override fun onComplete() {
        lD.hideLoadingDialog()
    }


    override fun onNext(t: T) {
        //验证返回的数据
        if (t != null)
            hND.onNext(t)

    }

    override fun onError(t: Throwable?) {
        lD.hideLoadingDialog()
        hND.onError(t)
        //显示网络失败
        if (showToast) {
            Toast.makeText(App.getContext(), t.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}


