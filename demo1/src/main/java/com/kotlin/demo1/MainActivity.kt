package com.kotlin.demo1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import example.com.kotlindemo.adapter.DemoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: DemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        swipeRefreshLayout.isEnableAutoLoadmore = true
        swipeRefreshLayout.refreshHeader = ClassicsHeader(this)
        swipeRefreshLayout.refreshFooter = ClassicsFooter(this)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.finishRefresh()
            var list: ArrayList<String> = ArrayList()
            list.add("1")
            list.add("12")
            list.add("13")
            list.add("14")
            adapter.addNewList(0, list)

        }

        swipeRefreshLayout.setOnLoadmoreListener {

            swipeRefreshLayout.finishLoadmore()

        }
        adapter = DemoAdapter(this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

    }
}
