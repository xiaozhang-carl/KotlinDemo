package example.com.kotlindemo.net

/**
 * Created by zhanghongqiang on 2017/5/20.
 * ToDo:传递网络返回数据的接口
 */
 abstract class HandleNetData<T> {
    abstract fun onNext(t: T);//返回正确的处理数据
    open fun misMatch(t: T){};//返回的数据不是想要的
    open fun onError(t: Throwable?){};//异常信息
}