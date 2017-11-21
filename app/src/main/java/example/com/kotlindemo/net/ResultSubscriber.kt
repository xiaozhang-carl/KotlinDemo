package example.com.kotlindemo.net

import android.util.Log
import android.widget.Toast
import example.com.kotlindemo.App
import example.com.kotlindemo.exception.ApiException
import example.com.kotlindemo.exception.ExceptionFactory
import example.com.kotlindemo.model.ClassCircle
import example.com.kotlindemo.utils.PreferencesUtil
import io.reactivex.subscribers.ResourceSubscriber


/**
 * Created by zhanghongqiang on 2017/5/20.
 */
/**
 *
 */
class ResultSubscriber<T>(private val hND: HandleNetData<T>
                          , private val lD: LoadingDialog
                          , private val showToast: Boolean = true)
    : ResourceSubscriber<T>() {

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
        if (t == null) {
            return
        }
        //根据状态码来判断返回的数据
        if (t is ClassCircle) {
            if (t.status == -500) {
                PreferencesUtil.getInstance(App.instance).putString("Set-Cookie","" )
                hND.onWrong(t)
                return
            }
        }
        hND.onRight(t)

    }

    override fun onError(t: Throwable) {
        lD.hideLoadingDialog()
        hND.onException(ExceptionFactory.create(t))
        Log.e("123",t.toString())
        //显示网络失败
        if (showToast) {
            (t as? ApiException)?.apply {
                // 登录失效
                if (errorCode.equals("3") || errorMsg.equals("illegal token")) {
//                                Intent(JueJinApp.instance, LoginActivity::class.java)
//                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                        .apply{
//                                            JueJinApp.instance.startActivity(this)
//                                        }
//                                LocalUser.userToken = null
//                                Toast.makeText(JueJinApp.instance, "登录已失效，请重新登录", Toast.LENGTH_SHORT).show()
                } else {
                    // toast 前， 确保在 UI 线程 !!!
                    Toast.makeText(App.instance, this.errorMsg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}


