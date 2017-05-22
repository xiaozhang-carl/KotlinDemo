package example.com.kotlindemo.net

import example.com.kotlindemo.User

/**
 * Created by zhanghongqiang on 2017/5/20.
 */
interface GitHubService{
    @retrofit2.http.GET("/repos/enbandari/Kotlin-Tutorials/stargazers")
    fun stargazers(): io.reactivex.Flowable<List<User>>
}