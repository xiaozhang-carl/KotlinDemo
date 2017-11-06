package example.com.kotlindemo

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.text.TextUtils
import android.widget.Toast

/**
 * Created by zhanghongqiang on 2017/5/23.
 * ToDo:拓展的方法
 */

object Extension {

    fun Activity.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
        if (TextUtils.isEmpty(message)){
            return
        }
        Toast.makeText(this as Context, message, duration).show()
    }

    fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
        if (TextUtils.isEmpty(message)){
            return
        }
        Toast.makeText(this as Context, message, duration).show()
    }


}