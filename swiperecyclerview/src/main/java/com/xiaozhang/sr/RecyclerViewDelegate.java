package com.xiaozhang.sr;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午11:18
 * ToDo:列表代理者
 */
public class RecyclerViewDelegate<T> extends RecyclerViewContract.RVPresenter<T> {


    public RecyclerViewDelegate(RecyclerViewContract.IFLoadData mLoadData, RecyclerViewContract.IFAdapter F) {
        super(mLoadData, F);
    }

    /**
     * @return
     */
    public static RecyclerViewDelegate with(RecyclerViewContract.IFLoadData mLoadData, RecyclerViewContract.IFAdapter F) {
        return new RecyclerViewDelegate(mLoadData, F);
    }


    public RecyclerViewDelegate recyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        linearLayoutManager();
        return this;
    }

    public RecyclerViewDelegate recyclerView(RecyclerView recyclerView, EmptyViewLisenter lisenter) {
        this.mRecyclerView = recyclerView;
        linearLayoutManager();
        initEmpty(lisenter);
        return this;
    }

    private void initEmpty(EmptyViewLisenter lisenter) {
        this.mEmptyViewLisenter = lisenter;
    }

    /**
     * 设置列表的头部
     *
     * @param headerView
     * @return
     */
    public RecyclerViewDelegate headerView(View headerView) {
        mHeaderView = headerView;
        return this;
    }

    /**
     * 设置列表加载的底部
     *
     * @param footerView
     * @return
     */
    public RecyclerViewDelegate footerView(View footerView) {
        mFooterView = footerView;
        return this;
    }


    /**
     * @param spanCount 网格布局的格数
     */
    public RecyclerViewDelegate<T> recyclerView(RecyclerView recyclerView, int spanCount, EmptyViewLisenter lisenter) {
        this.mRecyclerView = recyclerView;
        gridLayoutManager(spanCount);
        initEmpty(lisenter);
        return this;
    }

    /**
     * @param spanCount 网格布局的格数
     */
    public RecyclerViewDelegate<T> recyclerView(RecyclerView recyclerView, int spanCount) {
        this.mRecyclerView = recyclerView;
        gridLayoutManager(spanCount);
        return this;
    }


    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public RecyclerViewDelegate<T> recyclerView(RecyclerView recyclerView, int spanCount, int orientation) {
        this.mRecyclerView = recyclerView;
        staggeredGridLayoutManager(spanCount, orientation);
        return this;
    }


    private void linearLayoutManager() {
        //默认的layoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }


    /**
     * ToDo:网格列表
     *
     * @param spanCount
     */
    private void gridLayoutManager(int spanCount) {
        //GridLayoutManager
        GridLayoutManager layoutManager = new GridLayoutManager(mRecyclerView.getContext(), spanCount);
        //设置layoutManager
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * ToDo:交错网格
     *
     * @param spanCount
     */
    private void staggeredGridLayoutManager(int spanCount, int orientation) {
        // 交错网格布局管理器
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


    public RecyclerViewDelegate<T> build() {

        //新建适配器
        mAdapter = new RVAdapter();
        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mAdapter);
        //添加列表头部
        if (mHeaderView != null) {
            mHeaderViewRecyclerAdapter.setHeaderView(mHeaderView);
        }
        //添加列表尾部
        if (mFooterView != null) {
            mHeaderViewRecyclerAdapter.setFooterView(mFooterView);
        }

        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);

        return this;
    }

    @Override
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            //数据如果为空的话
            List<T> list = getDataList();
            for (int i = 0; i < list.size(); i++) {
                mAdapter.notifyItemChanged(i);
            }
        }
    }


    //重新加载数据
    @Override
    public void reLoadData() {
        if (mLoadDataImp != null) {
            mLoadDataImp.loadData();
        }
    }

    public void render(List newDatas) {
        if (newDatas == null) {
            return;
        }
        //清空原来的数据,防止数据重复添加
        clearData();
        //没有数据,显示空数据
        if (newDatas.size() != 0) {
            //加入新的数据
            if (mHeaderView == null) {
                mAdapter.addNewList(0, newDatas);
            } else {
                mAdapter.addNewList(1, newDatas);
            }
        }
        //显示内容页面
        showEmptyView();
    }

    public void showEmptyView() {
        if (mEmptyViewLisenter != null) {
            if (getDataList().size() == 0) {
                mEmptyViewLisenter.showEmptyView();
            } else {
                mEmptyViewLisenter.showContentView();
            }
        }
    }

}
