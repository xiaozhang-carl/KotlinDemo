package com.xiaozhang.sr;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        initLinearLayoutManager(recyclerView, LinearLayoutManager.VERTICAL);
        return this;
    }

    public RecyclerViewDelegate recyclerViewHorizontal(RecyclerView recyclerView) {
        initLinearLayoutManager(recyclerView, LinearLayoutManager.HORIZONTAL);
        return this;
    }

    public RecyclerViewDelegate recyclerView(RecyclerView recyclerView, StateFulLisenter lisenter) {
        initLinearLayoutManager(recyclerView, LinearLayoutManager.VERTICAL);
        initEmpty(lisenter);
        return this;
    }

    public RecyclerViewDelegate stateFulLisenter(StateFulLisenter lisenter) {
        initEmpty(lisenter);
        return this;
    }



    private void initEmpty(StateFulLisenter lisenter) {
        this.mStateFulLisenter = lisenter;
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
    public RecyclerViewDelegate<T> recyclerView(RecyclerView recyclerView, int spanCount, StateFulLisenter lisenter) {
        initGridLayoutManager(recyclerView,spanCount);
        initEmpty(lisenter);
        return this;
    }

    /**
     * @param spanCount 网格布局的格数
     */
    public RecyclerViewDelegate<T> recyclerView(RecyclerView recyclerView, int spanCount) {
        initGridLayoutManager(recyclerView,spanCount);
        return this;
    }


    /**
     * @param spanCount   交错网格的格子数
     * @param orientation
     * @return
     */
    public RecyclerViewDelegate<T> recyclerView(RecyclerView recyclerView, int spanCount, int orientation) {
        initStaggeredGridLayoutManager(recyclerView,spanCount,orientation);
        return this;
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
        if (mStateFulLisenter != null) {
            if (getDataList().size() == 0) {
                mStateFulLisenter.showEmptyView();
            } else {
                mStateFulLisenter.showContentView();
            }
        }
    }

}
