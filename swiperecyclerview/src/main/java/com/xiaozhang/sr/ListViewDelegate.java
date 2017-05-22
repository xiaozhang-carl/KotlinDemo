package com.xiaozhang.sr;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/3/1  下午4:58
 * ToDo:listView的代理者
 */
public class ListViewDelegate<T> extends ListViewContract.LVPresenter {


    //里面的列表
    private ListView listView;

    //没有数据将显示的提示
    private View emptyView;

    //头布局
    private View headerView;

    //底部布局
    private View footerView;


    //万用的适配器
    private MyAdapter myAdapter;


    public ListViewDelegate(ListViewContract.IFLoadData L, ListViewContract.IFAdapter adapter) {
        super(L, adapter);
    }


    public static ListViewDelegate with(ListViewContract.IFLoadData L, ListViewContract.IFAdapter adapter) {
        return new ListViewDelegate(L, adapter);
    }


    /**
     * @param listView
     */
    public ListViewDelegate listView(@NonNull ListView listView) {
        this.listView = listView;
        return this;
    }


    public ListViewDelegate emptyView(@NonNull View emptyView) {
        this.emptyView = emptyView;
        return this;
    }

    public ListViewDelegate headerView(@NonNull View headerView) {
        this.headerView = headerView;
        return this;
    }

    public ListViewDelegate footerView(@NonNull View footerView) {
        this.footerView = footerView;
        return this;
    }


    public ListViewDelegate build() {

        if (this.headerView != null) {
            this.listView.addHeaderView(headerView, null, false);
        }
        if (this.footerView != null) {
            this.listView.addFooterView(footerView, null, false);
        }
        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);
        return this;
    }

    @Override
    public void render(List list) {
        if (list == null || list.size() == 0) {
            if (emptyView != null) {
                emptyView.setVisibility(View.VISIBLE);
            }
            return;
        }
        getDataList().clear();
        myAdapter.addNewList(list);
    }

    public List<T> getDataList() {
        return myAdapter.dataList;
    }

    public void notifyDataSetChanged() {
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void reLoadData() {
        clearData();
        mLoadData.loadData();
    }

    @Override
    public void clearData() {
        getDataList().clear();
        myAdapter.notifyDataSetChanged();
    }

}
