package com.xiaozhang.sr;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午10:49
 * ToDo:列表的合约者
 */
public interface RecyclerViewContract {

    //加载数据
    interface IFLoadData {
        void loadData();
    }

    //适配器使用
    interface IFAdapter<T> {

        //显示数据
        void updateView(@NonNull T data, @NonNull ViewDataBinding binding, int position);

        //这里的使用一定要注意,用第二个参数来判断
        ViewDataBinding createView(ViewGroup parent, int type);

    }

    //适配器使用
    interface IFTypeAdapter<T> extends IFAdapter<T> {

        //可以根据数据类型来显示不同的item
        int getItemViewType(int position);

    }

    //代理者
    abstract class RVPresenter<T> {

        public RVPresenter(IFLoadData loadData, IFAdapter adapter) {
            mLoadDataImp = loadData;
            mAdapterImp = adapter;
        }

        //暴露给外界的接口是实现者
        RecyclerViewContract.IFAdapter mAdapterImp = null;

        RecyclerViewContract.IFLoadData mLoadDataImp = null;

        //空布局的
        EmptyViewLisenter mEmptyViewLisenter;

        //列表RecyclerView头部
        View mHeaderView;

        //列表RecyclerView底部
        View mFooterView;

        //xml里面的列表
        RecyclerView mRecyclerView;

        //万用的适配器
        RVAdapter mAdapter;

        //包装adpter的适配器
        HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter;

        //子类重新刷新数据
        public abstract void reLoadData();

        //子类展示数据
        public abstract void render(List<T> list);

        //子类通知刷新数据
        public abstract void notifyDataSetChanged();

        //查看列表数据
        public abstract void showEmptyView();

        //获取数据列表
        public List<T> getDataList() {
            return mAdapter.mDatas;
        }

        //数据存在于列表中的位置
        public int indexOf(T t) {
            return getDataList().indexOf(t);
        }

        /**
         * 清空数据列表
         */
        public void clearData() {

            if (mAdapter != null) {
                //加入新的数据
                if (mHeaderView == null) {
                    mAdapter.clearList(0);
                } else {
                    mAdapter.clearList(1);
                }
            }
        }

        /**
         *
         * @param position
         */
        public void notifyItemChanged(int position) {
            if (position < 0) {
                return;
            }
            if (mHeaderView == null) {
                mAdapter.notifyItemChanged(position);
            } else {
                mAdapter.notifyItemChanged(position + 1);
            }
        }

        /**
         *
         * @param obj
         */
        public void notifyItemChanged(Object obj) {
            if (obj== null) {
                return;
            }
            T t = (T) obj;
            int position = indexOf(t);
            notifyItemChanged(position);
        }

        /**
         *
         * @param obj
         */
        public void notifyItemRangeRemoved(Object obj) {
            if (obj == null) {
                return;
            }
            T t = (T)obj;
            int position = indexOf(t);
            if (position == -1) {
                return;
            }
            if (mAdapter != null) {
                getDataList().remove(position);
                if (mHeaderView == null) {
                    mAdapter.notifyItemRangeRemoved(position, 1);
                } else {
                    mAdapter.notifyItemRangeRemoved(position + 1, 1);
                }
                //是否显示空数据
                showEmptyView();
            }
        }

        /**
         *
         * @param obj
         */
        public void notifyItemRangeInserted(Object obj) {
            notifyItemRangeInserted(getDataList().size(), obj);
        }

        /**
         *
         * @param position
         * @param obj
         */
        public void notifyItemRangeInserted(int position, Object obj) {
            if (position < 0) {
                return;
            }
            T t = (T) obj;
            if (mAdapter != null) {
                getDataList().add(position, t);
                if (mHeaderView == null) {
                    mAdapter.notifyItemRangeInserted(position, 1);
                    mRecyclerView.scrollToPosition(position);
                } else {
                    mAdapter.notifyItemRangeInserted(position + 1, 1);
                    mRecyclerView.scrollToPosition(position + 1);
                }

                //是否显示空数据
                showEmptyView();
            }

        }


        /**
         * @param position 当前t在列表dataList的位置
         */
        public void notifyItemRangeRemoved(int position) {
            if (position < 0) {
                return;
            }
            if (mAdapter != null) {
                //从数据源中移除数据
                getDataList().remove(position);
                //一定要调用这个方法,因为RecyclerView添加了头部,所以这个position+1
                if (mHeaderView == null) {
                    mAdapter.notifyItemRangeRemoved(position, 1);
                } else {
                    mAdapter.notifyItemRangeRemoved(position + 1, 1);
                }

                //是否显示空数据
                showEmptyView();
            }

        }

        //万用的适配器（需要结合databinding使用）
        class RVAdapter<T> extends RecyclerView.Adapter<RVViewHolder<T>> {

            ArrayList<T> mDatas = new ArrayList<>();

            @Override
            public RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                //调用接口的方法
                ViewDataBinding binding = mAdapterImp.createView(parent, viewType);
                RVViewHolder viewHolder = new RVViewHolder(binding.getRoot());
                viewHolder.mViewDataBinding = binding;
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(RVViewHolder holder, int position) {
                holder.setData(getItem(position), position);
            }

            private T getItem(int position) {
                return mDatas.get(position);
            }

            @Override
            public int getItemCount() {
                return mDatas != null ? mDatas.size() : 0;
            }

            @Override
            public int getItemViewType(int position) {
                if (mAdapterImp instanceof IFTypeAdapter) {
                    return ((IFTypeAdapter) mAdapterImp).getItemViewType(position);
                }
                //调用接口的方法
                return super.getItemViewType(position);
            }

            public void clearList(int positionStart) {
                //防止刷新闪烁的出现
                int itemCount = mDatas.size();
                mDatas.clear();
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            public void addNewList(int insertPosition, List<T> list) {
                //防止刷新闪烁的出现
                if (list != null && list.size() > 0) {
                    mDatas.addAll(list);
                    notifyItemRangeInserted(insertPosition, list.size());
                }
            }

        }

        /**
         * RecyclerView万用的适配器
         */
        class RVViewHolder<T> extends RecyclerView.ViewHolder {


            ViewDataBinding mViewDataBinding;

            public RVViewHolder(View itemView) {
                super(itemView);
            }

            public void setData(T data, int position) {
                //调用接口的方法
                mAdapterImp.updateView(data, mViewDataBinding, position);
            }
        }

    }
}
