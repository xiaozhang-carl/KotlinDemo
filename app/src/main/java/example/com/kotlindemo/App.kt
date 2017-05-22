package example.com.kotlindemo

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho


/**
 * Created by zhanghongqiang on 2017/5/22.
 */
open class App : Application() {

    //static的方式
    companion object {

        private lateinit var app: Application

        fun getContext(): Context {
            return app
        }

    }


    override fun onCreate() {
        super.onCreate()
        app = this
        debug()
    }


    fun debug() {
        //内存泄漏
        /*OkHttp+Stetho+Chrome进行网络调试,发布的时候去掉:浏览器chrome://inspect/#devices*/
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }

    }
}