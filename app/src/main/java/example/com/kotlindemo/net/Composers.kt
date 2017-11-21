package example.com.kotlindemo.net

import android.util.Log
import android.widget.Toast
import example.com.kotlindemo.App
import example.com.kotlindemo.exception.ApiException
import example.com.kotlindemo.exception.ExceptionFactory
import example.com.kotlindemo.model.ClassCircle
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object Composers {

    private val TAG = "Composers"

    /**
     * 普通的 ResponseBody 格式(非json), 处理 404 等 errorCode
     * {"status": -500,"msg": "no login"}
     */
    fun <T> composeWithoutResponse(): FlowableTransformer<T, T> {
        return FlowableTransformer { observable ->
            observable
                    .onBackpressureDrop()
                    .onErrorResumeNext { throwable: Throwable ->
                        Flowable.error(ExceptionFactory.create(throwable))
                    }

                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterNext {
                        if (it is ClassCircle){
                            if(it.status==-500){
                                Toast.makeText(App.instance, "登录已失效，请重新登录", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .doOnError { e: Throwable ->
                        Log.e(TAG, e.toString())
                        (e as? ApiException)?.apply {
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

    /**
     * 接口数据缓存策略:
     *      1.检查缓存数据, 若有则先显示缓存
     *      2.回调网络数据, 若成功则覆盖先前的缓存数据
     *      3.拉取数据成功, 缓存数据到本地
     *
     * @param key 接口标识, 如: "接口名" + "请求参数(Json格式)"
     */
    fun <T> cache(key: String): ObservableTransformer<in T, out T>? {
        return null
//        return ObservableTransformer { observable ->
//            Observable.create<T> { emitter ->
//                // 1.检查缓存数据, 若有则先显示缓存
//                val localData = CXCacheFileManager.get(CXBaseApplication.INSTANCE).getAsObject(key)
//                if (localData != null)
//                    emitter.onNext(localData as T)
//
//                // 2.回调网络数据, 若成功则覆盖先前的缓存数据
//                observable.subscribe({ data ->
//                    emitter.onNext(data)
//
//                    // 3.拉取数据成功, 缓存数据到本体
//                    CXCacheFileManager.get(CXBaseApplication.INSTANCE).put(key, data as Serializable)
//
//                }, { error ->
//                    emitter.onError(error)
//
//                }, {
//                    emitter.onComplete()
//                })
//            }
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//        }
    }
}
