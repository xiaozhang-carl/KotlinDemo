package example.com.kotlindemo.net

import android.databinding.ObservableField
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by zhanghongqiang on 2017/5/22.
 */
/**
 *@flowable
 *@nextListener
 *@dialogImp
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


fun <T> toObservable(observableField: ObservableField<T>): ObservableSource<T> {
    return Flowable.create(FlowableOnSubscribe<T> { asyncEmitter ->
        val callback = object : android.databinding.Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(dataBindingObservable: android.databinding.Observable, propertyId: Int) {
                if (dataBindingObservable === observableField) {
                    asyncEmitter.onNext(observableField.get())
                }
            }
        }

        observableField.addOnPropertyChangedCallback(callback)
        asyncEmitter.setCancellable { observableField.removeOnPropertyChangedCallback(callback) }
    }, BackpressureStrategy.LATEST).toObservable()
}


