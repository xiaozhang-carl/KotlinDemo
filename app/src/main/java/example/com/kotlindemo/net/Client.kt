package example.com.kotlindemo.net

import android.text.TextUtils
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import example.com.kotlindemo.App
import example.com.kotlindemo.BuildConfig
import example.com.kotlindemo.utils.PreferencesUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by zhanghongqiang on 2017/5/20.
 * ToDo：网络服务
 */
object Client {

    val baseUrl = "https://api.github.com"
    val dongDianUrl = "http://weixiaologin.dongdianyun.com:9001/"

    var gitHubService = getApi(baseUrl, GitHubService::class.java)
    var dongDian = getApi(dongDianUrl, DongDianService::class.java,createInterceptor())

    fun createInterceptor():Interceptor{
        //OkHttp拦截器,用来添加头部信息
        val mTokenInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            //获取保存的token
            val Cookie = PreferencesUtil.getInstance(App.instance).getString("Set-Cookie")
            if (TextUtils.isEmpty(Cookie)) {
                return@Interceptor chain.proceed(originalRequest)
            }
            //添加到头部
            val newRequest = chain.request()
                    .newBuilder()
                    .header("Cookie", Cookie)
                    .build()
            chain.proceed(newRequest)
        }

        return mTokenInterceptor
    }

    private var CONNECT_TIMEOUT_SECONDS = 20L
    private var READ_TIMEOUT_SECONDS = 20L
    private var WRITE_TIMEOUT_SECONDS = 20L


    private fun create(customInterceptor: Interceptor? = null, enableLog: Boolean = true): OkHttpClient {
        val sslParams = HttpsUtil.getSslSocketFactory(null, null, null)
        val loggingInterceptor = HttpLoggingInterceptor { chain, msg ->
            //打印网络请求的数据
//            Log.d("123", msg)
        }.apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }


        return OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .apply {
                    //DEBUG调试模式下，chrome来查看数据
                    if (BuildConfig.DEBUG)
                        this.addNetworkInterceptor(StethoInterceptor())
                }
                .apply {
                    if (customInterceptor != null)
                        this.addInterceptor(customInterceptor)
                }
                .apply {
                    if (BuildConfig.DEBUG)
                        this.addInterceptor(loggingInterceptor)
                }
                .build()

    }


    private fun <T> getApi(baseUrl: String, clazz: Class<T>, customInterceptor: Interceptor? = null): T {
        if (baseUrl.isEmpty()) {
            throw IllegalArgumentException("baseUrl is empty")
        }
        return Retrofit.Builder()
                .client(create(customInterceptor))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(clazz)
    }
}