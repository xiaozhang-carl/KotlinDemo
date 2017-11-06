package example.com.kotlindemo.rx

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject


/**
 * Created by zhanghongqiang on 2017/5/22.
 */
object RxBus {

    // 主题
    private var bus: Subject<Any> = PublishSubject.create()


    // 提供了一个新的事件
    fun post(any: Any) {
        bus.onNext(any)
    }


    fun <T> register(eventClass: Class<T>, onNext: Consumer<T>): Disposable {
        return bus.filter(object : Predicate<Any> {
            override fun test(any: Any): Boolean {
                return any.javaClass.equals(eventClass);
            }
        })
                .map(object : Function<Any, T> {
                    override fun apply(any: Any?): T {
                        return any as T
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext)
    }
}