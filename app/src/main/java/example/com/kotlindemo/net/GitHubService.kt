package example.com.kotlindemo.net

import example.com.kotlindemo.model.User

/**
 * Created by zhanghongqiang on 2017/5/20.
 * ToDo：网络接口
 */
interface GitHubService{
    @retrofit2.http.GET("/repos/enbandari/Kotlin-Tutorials/stargazers")
    fun stargazers(): io.reactivex.Flowable<List<User>>
}