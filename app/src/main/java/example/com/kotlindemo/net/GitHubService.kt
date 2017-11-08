package example.com.kotlindemo.net

import example.com.kotlindemo.model.User
import io.reactivex.Flowable
import retrofit2.http.GET

/**
 * Created by zhanghongqiang on 2017/5/20.
 * ToDo：网络接口
 */
interface GitHubService{
    @GET("/repos/enbandari/Kotlin-Tutorials/stargazers")
    fun stargazers(): Flowable<List<User>>
}