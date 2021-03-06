package com.xiaozhang.sr;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xiaozhang.sr.rv.LoadingMoreView;

import java.util.List;


/**
 * Created by zhanghongqiang on 2016/10/31  下午2:38
 * ToDo:列表的数据加载
 */
public class SwipeRecyclerViewDelegate<T> extends RecyclerViewContract.RVPresenter<T> {

    //谷歌下拉刷新的布局
    private SwipeRefreshLayout mSwipeLayout;
    //列表RecyclerView底部下面的加载更多布局
    private View mLoadingView;
    //分页,从0开始
    private int mPage = 0;
    //每一页的item个数,默认20条
    private int mCount = 20;
    //默认能够下拉刷新
    private boolean mPullRefreshEnabled = true;
    //默认能够加载更多
    private boolean mLoadingMoreEnabled = true;
    //RecyclerView的滚动监听
    private RecyclerViewScrollListener mScrollListener;
    //加载分页，第一页之后的哪一个页面没有跟多数据
    private boolean noMoreData = false;

    public SwipeRecyclerViewDelegate(RecyclerViewContract.IFLoadData loadData, RecyclerViewContract.IFAdapter adapter) {
        super(loadData, adapter);
    }

    /**
     * @return
     */
    public static SwipeRecyclerViewDelegate with(RecyclerViewContract.IFLoadData L, RecyclerViewContract.IFAdapter F) {
        return new SwipeRecyclerViewDelegate(L, F);
    }

    /**
     * 默认的单行列表竖向显示
     *
     * @param swipeRefreshLayout
     * @param recyclerView
     * @return
     */
    public SwipeRecyclerViewDelegate recyclerView(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView) {
        this.mSwipeLayout = swipeRefreshLayout;
        initLinearLayoutManager(recyclerView, LinearLayoutManager.VERTICAL);
        return this;
    }

    /**
     * 默认的单行列表竖向显示
     *
     * @param swipeRefreshLayout
     * @param recyclerView
     * @return
     */
    public SwipeRecyclerViewDelegate recyclerView(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, StateFulLisenter lisenter) {
        this.mSwipeLayout = swipeRefreshLayout;
        initEmpty(lisenter);
        initLinearLayoutManager(recyclerView, LinearLayoutManager.VERTICAL);
        return this;
    }


    private void initEmpty(StateFulLisenter lisenter) {
        this.mStateFulLisenter = lisenter;
    }

    /**
     * 网格布局
     *
     * @param swipeRefreshLayout
     * @param recyclerView
     * @param spanCount          网格布局的格数
     * @return
     */
    public SwipeRecyclerViewDelegate recyclerView(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, int spanCount) {
        this.mSwipeLayout = swipeRefreshLayout;
        initGridLayoutManager(recyclerView,spanCount);
        return this;
    }

    /**
     * 网格布局
     *
     * @param swipeRefreshLayout
     * @param recyclerView
     * @param spanCount          网格布局的格数
     * @return
     */
    public SwipeRecyclerViewDelegate recyclerView(SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, int spanCount, StateFulLisenter lisenter) {
        this.mSwipeLayout = swipeRefreshLayout;
        initEmpty(lisenter);
        initGridLayoutManager(recyclerView,spanCount);
        return this;
    }



    /**
     * 设置列表加载的布局
     *
     * @param loadingView 自定义的列表底部加载布局
     * @return
     */
    public SwipeRecyclerViewDelegate setLoadingView(View loadingView) {
        mLoadingView = loadingView;
        return this;
    }

    /**
     * 设置列表的头部
     *
     * @param headerView
     * @return
     */
    public SwipeRecyclerViewDelegate setHeaderView(View headerView) {
        mHeaderView = headerView;
        return this;
    }

    /**
     * 设置列表加载的底部
     *
     * @param footerView
     * @return
     */
    public SwipeRecyclerViewDelegate setFooterView(View footerView) {
        mFooterView = footerView;
        return this;
    }


    /**
     * @param pullRefresh 头部是否下拉
     * @param loadingMore 底部加载更多
     * @return
     */
    public SwipeRecyclerViewDelegate<T> cancelRefresh(boolean pullRefresh, boolean loadingMore) {
        this.mPullRefreshEnabled = pullRefresh;
        this.mLoadingMoreEnabled = loadingMore;
        return this;
    }

    public SwipeRecyclerViewDelegate<T> stateFulLisenter(StateFulLisenter lisenter) {
        initEmpty(lisenter);
        return this;
    }



    public SwipeRecyclerViewDelegate build() {

        initSwipeRefreshLayout();

        mAdapter = new RVAdapter();

        mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(mAdapter);
        //添加列表头部
        if (mHeaderView != null) {
            mHeaderViewRecyclerAdapter.setHeaderView(mHeaderView);
        }
        //添加列表尾部
        if (this.mFooterView != null) {
            mHeaderViewRecyclerAdapter.setFooterView(mFooterView);
        }

        //需要加载更多的
        initLoadMoreView();

        mRecyclerView.setAdapter(mHeaderViewRecyclerAdapter);
        return this;
    }

    //https://github.com/hanks-zyh/SwipeRefreshLayout
    private void initSwipeRefreshLayout() {
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
//        mSwipeLayout.setProgressViewOffset(true, 50, 200);
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        mSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(
                R.color.color_1,
                R.color.color_2,
                R.color.color_3,
                R.color.color_4);


        // 设定下拉圆圈的背景
//        mSwipeLayout.setProgressBackgroundColorSchemeResource(R.color.cardview_dark_background);
        //设置手势下拉刷新的监听
        if (mPullRefreshEnabled) {
            mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    //下拉刷新使用
                    mPage = 0;
                    //初次加载，加载更多的不显示
                    showLoadMoreView(true);
                    //网络请求数据
                    if (mLoadDataImp != null) {
                        mLoadDataImp.loadData();
                    }
                }
            });
        } else {
            // 通过 setEnabled(false) 禁用下拉刷新
            mSwipeLayout.setEnabled(false);
        }

    }

    private void initLoadMoreView() {
        if (mLoadingMoreEnabled) {
            if (mScrollListener == null) {
                mScrollListener = new RecyclerViewScrollListener(mSwipeLayout, mLoadDataImp);
            }
            mRecyclerView.addOnScrollListener(mScrollListener);
            //加载更多的样式
            if (mLoadingView == null) {
                //默认的样式,
                mLoadingView = new LoadingMoreView(mRecyclerView.getContext());
            }
            mScrollListener.setLoadingView(mLoadingView);
            mHeaderViewRecyclerAdapter.setLoadingView(mLoadingView);
        }
    }


    /**
     * 下一页,获取数据的时候调用
     *
     * @return
     */
    public int nextPage() {
        return ++mPage;
    }

    /**
     * 下一页,获取当前的分页
     *
     * @return
     */
    public int getPage() {
        return mPage;
    }

    /**
     * 需要显示网络错误页面
     *
     * @return
     */
    public boolean needShowErrorView() {
        return mPage < 1 && getDataList().size() == 0;
    }

    /**
     * 返回每页的条数
     *
     * @return
     */
    public int getPageSize() {
        return mCount;
    }

    /**
     * 重新加载数据
     */
    @Override
    public void reLoadData() {
        //页面一定要设置为初始页
        mPage = 0;
        mSwipeLayout.setRefreshing(true);
        //网络请求数据
        if (mLoadDataImp != null) {
            mLoadDataImp.loadData();
        }
    }

    @Override
    public void render(List list) {
        //刷新完成,隐藏进度条...
        refreshComplete();
        if (list == null) {
            return;
        }

        //下拉刷新,多次请求首页的话,清空数据
        if (mPage == 1 || mPage == 0) {
            //首先清空原有数据
            clearData();
            if (list.size() != 0) {
                //有数据的话,清空原来的数据,防止数据重复添加。
                getDataList().addAll(list);
            }
            mAdapter.notifyDataSetChanged();
            showEmptyView();
        } else {
            //加入新的数据
            //一定要调用这个方法,因为RecyclerView添加了头部,所以这个position+1
            int position = getDataList().size();
            if (mHeaderView == null) {
                mAdapter.addNewList(position, list);
            } else {
                mAdapter.addNewList(position + 1, list);
            }
        }

        //如果数据为0.把页码回置
        if (list.size() == 0) {
            mPage--;
        }

    }


    //网络连接失败的话需要调用，刷新完成，页码退回
    public void onError() {
        refreshComplete();
        mPage--;
    }

    //刷新完成,隐藏进度条...
    public void refreshComplete() {
        //因为dialog显示的时间是500，所以这里延时显示
        mSwipeLayout.setRefreshing(false);
        //加载更多
        showLoadMoreView(false);
    }

    //是否显示加载更多的布局
    private void showLoadMoreView(boolean bool) {
        if (mLoadingMoreEnabled) {
            mScrollListener.setLoading(bool);
            //加载更多的设置
            if (mLoadingView instanceof LoadingMoreView) {
                //如果列表
                LoadingMoreView loadingView = (LoadingMoreView) mLoadingView;
                if (noMoreData) {
                    loadingView.setStatus(LoadingMoreView.STATUS_INIT);
                } else {
                    loadingView.setStatus(LoadingMoreView.STATUS_INIT);
                }

            } else {
                mLoadingView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        if ( mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * https://github.com/AlexSmille/alex_mahao_sample
     */
    public SwipeRecyclerViewDelegate setScrollListener(RecyclerViewScrollListener scrollListener) {
        mScrollListener = scrollListener;
        return this;
    }

    //查看列表数据
    @Override
    public void showEmptyView() {
        if (mStateFulLisenter != null) {
            if (getDataList().size() == 0) {
                mStateFulLisenter.showEmptyView();
            } else {
                mStateFulLisenter.showContentView();
            }
        }
    }


    public void setPage(int page) {
        mPage = page;
    }

}
