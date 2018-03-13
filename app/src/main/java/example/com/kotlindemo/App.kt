package example.com.kotlindemo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import com.facebook.stetho.Stetho
import com.tencent.smtt.sdk.QbSdk
import java.util.*


/**
 * Created by zhanghongqiang on 2017/5/22.
 * ToDo:项目的app
 */
open class App : Application(), Application.ActivityLifecycleCallbacks {

    //伴生对象
    companion object {

        lateinit var instance: Context

        //管理activity的任务栈
        private val mExistedActivitys = Stack<Activity>()

    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        initX5WebView()
        debug()
    }

    private fun initX5WebView() {
        val cb = object : QbSdk.PreInitCallback {

            override fun onViewInitFinished(arg0: Boolean) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0)
            }

            override fun onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb)
    }


    fun debug() {
        //内存泄漏
        /*OkHttp+Stetho+Chrome进行网络调试,发布的时候去掉:浏览器chrome://inspect/#devices*/
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
        if (null != activity) {
            mExistedActivitys.remove(activity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, p1: Bundle?) {
        if (null != activity) {
            // 把新的 activity 添加到最前面，和系统的 activity 堆栈保持一致
            mExistedActivitys.add(activity)
        }
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return mExistedActivitys.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishLastActivity() {
        val activity = mExistedActivitys.lastElement()
        if (activity != null) {
            finishActivity(activity)
        }

    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            mExistedActivitys.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        val activities = Stack<Activity>()
        //找到任务stack的activity
        for (activity in mExistedActivitys) {
            if (activity.javaClass == cls) {
                activities.add(activity)
            }
        }
        finishAllActivity(activities)
    }

    /**
     * 结束指定类名的Activity
     */
    fun hasActivity(cls: Class<*>): Boolean {
        //找到任务stack的activity
        for (activity in mExistedActivitys) {
            if (activity.javaClass == cls) {
                return true
            }
        }
        return false
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        finishAllActivity(mExistedActivitys)
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity(mExistedActivitys: Stack<Activity>) {
        var i = 0
        val size = mExistedActivitys.size
        while (i < size) {
            if (null != mExistedActivitys[i]) {
                mExistedActivitys[i].finish()
            }
            i++
        }
        mExistedActivitys.clear()
    }

    /**
     * 退出应用程序
     */
    fun exit() {
        try {
            finishAllActivity()
            System.exit(0)
        } catch (e: Exception) {
        }

    }

}