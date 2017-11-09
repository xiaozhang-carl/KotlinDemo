package example.com.kotlindemo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * @作者： 张宏强
 * @时间：2015-6-13下午1:39:53
 * @作用：侧滑容器的适配器
 */
class ViewPagerAdapter(private val fragments: ArrayList<Fragment>?, private val manager: FragmentManager) : PagerAdapter() {

    override fun getCount(): Int {

        return fragments?.size ?: 0
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view: View? = null
        val fragment = fragments!![position]
        if (!fragment.isAdded) {
            val transaction = manager.beginTransaction()
            val name = fragment.javaClass.name
            transaction.add(fragment, name)
            transaction.commitAllowingStateLoss()
            manager.executePendingTransactions()
        }
        view = fragment.view
        // 防止重复添加
        if (view!!.parent == null) {
            container.addView(view)
        }
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(fragments!![position].view)
    }
}
