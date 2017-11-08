package example.com.kotlindemo.rx

import android.databinding.Observable
import android.databinding.ObservableField
import example.com.kotlindemo.net.Composers
import example.com.kotlindemo.net.HandleNetData
import example.com.kotlindemo.net.LoadingDialog
import example.com.kotlindemo.net.ResultSubscriber
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


/**
 * Created by zhanghongqiang on 2017/5/22.
 * ToDo：网络访问的包装
 */
/**
 *@flowable  网络接口
 *@nextListener 传递数据
 *@dialogImp  如果不传递的话，就重写实现方法
 *@showToast  默认显示toast，
 */
fun <T> rx(flowable: Flowable<T>,
           nextListener: HandleNetData<T>,
           dialogImp: LoadingDialog = object : LoadingDialog {
               override fun hideLoadingDialog() {
               }

               override fun showLoadingDialog() {
               }
           },
           showToast: Boolean = true): Disposable {

    return flowable
            .onBackpressureDrop()  //加上背压策略
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(ResultSubscriber(nextListener, dialogImp, showToast))
}


/**
 *@flowable  网络接口
 *@nextListener 传递数据
 *@dialogImp  如果不传递的话，就重写实现方法
 *@showToast  默认显示toast，
 */
fun <T> rx2(flowable: Flowable<T>,
           onNext: Consumer<T>,
           onError: Consumer<Throwable> = object : Consumer<Throwable> {
               override fun accept(t: Throwable?) {

               }

           }): Disposable {

    return flowable.compose(Composers.composeWithoutResponse())
            .subscribe(onNext, onError)
}


fun <T> toObservable(observableField: ObservableField<T>): ObservableSource<T> {
    return Flowable.create(FlowableOnSubscribe<T> { asyncEmitter ->
        val callback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(dataBindingObservable: Observable, propertyId: Int) {
                if (dataBindingObservable === observableField) {
                    asyncEmitter.onNext(observableField.get())
                }
            }
        }

        observableField.addOnPropertyChangedCallback(callback)
        asyncEmitter.setCancellable { observableField.removeOnPropertyChangedCallback(callback) }
    }, BackpressureStrategy.LATEST).toObservable()
}


