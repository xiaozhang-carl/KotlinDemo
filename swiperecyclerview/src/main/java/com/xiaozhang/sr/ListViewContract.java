package com.xiaozhang.sr;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghongqiang on 16/7/20  上午10:49
 * ToDo:列表的合约者
 */
public interface ListViewContract {

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
        int getViewTypeCount();

        //可以根据数据类型来显示不同的item,Note: Integers must be in the range 0 to {@link #getViewTypeCount} - 1.返回值只能在 0 ~ (getViewTypeCount - 1) 之间
        int getItemViewType(int position);

    }

    //代理者
    abstract class LVPresenter<T> {

        public LVPresenter(IFLoadData loadData, IFAdapter adapter) {
            mLoadData = loadData;
            mAdapter = adapter;
        }

        //暴露给外界的接口是实现者
        ListViewContract.IFAdapter mAdapter = null;

        ListViewContract.IFLoadData mLoadData = null;


        public abstract void reLoadData();

        public abstract void render(List<T> list);

        public abstract void clearData();


        /**
         * listView万用的适配器,
         */
        class MyAdapter extends BaseAdapter {

            final ArrayList<T> dataList = new ArrayList<>();

            @Override
            public int getCount() {
                return dataList.size();
            }

            @Override
            public T getItem(int position) {
                return dataList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                if (mAdapter instanceof IFTypeAdapter) {
                    return ((IFTypeAdapter) mAdapter).getViewTypeCount();
                }
                return super.getViewTypeCount();
            }


            @Override
            public int getItemViewType(int position) {
                if (mAdapter instanceof IFTypeAdapter) {
                    return ((IFTypeAdapter) mAdapter).getItemViewType(position);
                }
                return super.getItemViewType(position);
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewDataBinding binding = null;
                if (convertView == null) {
                    binding = mAdapter.createView(parent, getItemViewType(position));
                    convertView = binding.getRoot();
                    convertView.setTag(binding);
                } else {
                    binding = (ViewDataBinding) convertView.getTag();
                }
                mAdapter.updateView(getItem(position), binding, position);
                return binding.getRoot();
            }


            public BaseAdapter getAdapter() {
                return this;
            }


            public void clearList() {
                dataList.clear();
                notifyDataSetChanged();
            }

            public void addNewList(List<T> list) {
                if (list != null && list.size() > 0) {
                    dataList.addAll(list);
                    notifyDataSetChanged();
                }
            }
        }
    }
}
