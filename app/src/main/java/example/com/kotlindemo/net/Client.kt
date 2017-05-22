package example.com.kotlindemo.net

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import example.com.kotlindemo.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by zhanghongqiang on 2017/5/20.
 */
object Client{

    var gitHubService= gitHubService()

    private fun gitHubService(): GitHubService{
        //OkHttp拦截器,用来添加头部信息
        val builder = OkHttpClient.Builder()
        builder.retryOnConnectionFailure(true)
                .connectTimeout(5,TimeUnit.SECONDS )
                .addNetworkInterceptor( object : Interceptor{
                    override fun intercept(chain: Interceptor.Chain): Response {
                        //获取保存的token
                        //添加到头部
                        val newRequest = chain.request()
                                .newBuilder()
                                .build()
                        return chain.proceed(newRequest)
                    }
                })


        //设置chrome调试
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(StethoInterceptor())
        }
        return Retrofit.Builder()
                //绑定地址
                .baseUrl("https://api.github.com")
                //okhttp
                .client(builder.build())
                //gson转换工厂
                .addConverterFactory(GsonConverterFactory.create())
                //解析rxjava2的适配器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(GitHubService::class.java)
    }
}