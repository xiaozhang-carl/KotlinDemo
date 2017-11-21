package example.com.kotlindemo.net

import example.com.kotlindemo.model.ClassCircle
import example.com.kotlindemo.model.LogIn
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by zhanghongqiang on 2017/11/9 下午1:51
 * ToDo:
 */
interface DongDianService {
    @FormUrlEncoded
    @POST("MicroSchool/talkRecord_findByUserId.action")
    fun microSchool(
            @Field("start")start: Int,
            @Field("limit")limit: Int
    ): Flowable<ClassCircle>


    /**
     * @param identifyCode
     * @param roleType
     * @param userName
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("MicroSchool/user_login.action")
     fun login(
            @Field("identifyCode") identifyCode: String,
            @Field("roleType") roleType: Int,
            @Field("userName") userName: String,
            @Field("password") password: String
    ): Call<Response<LogIn>>
}