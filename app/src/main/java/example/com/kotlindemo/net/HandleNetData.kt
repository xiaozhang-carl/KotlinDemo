package example.com.kotlindemo.net

/**
 * Created by zhanghongqiang on 2017/5/20.
 */
interface HandleNetData<T>{
   fun onNext(t:T);//返回正确的处理数据
   fun misMatch(t:T);//返回的数据不是想要的
   fun onError(t:Throwable?);//异常信息
}