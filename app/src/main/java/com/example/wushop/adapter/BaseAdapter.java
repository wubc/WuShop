package com.example.wushop.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wushop.activity.WaresDetailsActivity;
import com.example.wushop.bean.Wares;
import com.example.wushop.widget.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * BaseAdapter封装
 * T 数据类型
 * H BaseViewHolder
 */

public abstract class BaseAdapter<T,H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected int mLayoutResId;

    protected OnItemClickListenner listener = null;


    public interface OnItemClickListenner{
        void onItemClick(View view,int positon);
    }

    public void setOnItemClickListenner(OnItemClickListenner listener){
        this.listener = listener;
    }

    public BaseAdapter(Context mContext,int mLayoutResId){
        this(mContext, null, mLayoutResId);
    }

    public BaseAdapter(Context context,List<T> datas,int layoutResId) {

        this.mContext = context;
        this.mDatas = datas == null ? new ArrayList<T>() : datas;
        this.mLayoutResId = layoutResId;
        mInflater = LayoutInflater.from(context);
    }

    public List<T> getDatas(){
        return mDatas;
    }

    //删除数据
    public void clearData(){

        if (mDatas == null || mDatas.size() <= 0)
            return;

        for (Iterator it = mDatas.iterator(); it.hasNext(); ){
            T t  = (T) it.next();
            int position = mDatas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }

    }

    public void addData(List<T> datas){
        addData(0,datas);
    }

    //添加数据
    public void addData(int i, List<T> datas) {

        if (datas != null && datas.size() > 0){
            this.mDatas.addAll(datas);
            this.notifyItemRangeChanged(i,datas.size());
        }else {
            mDatas.clear();
        }
    }

    //刷新数据
    public void refrenshData(List<T> list){
        if (list != null && list.size() > 0){
            clearData();

            int size = list.size();

            for (int i=0; i< size ; i++){
                mDatas.add(i, list.get(i));
                notifyItemInserted(i);
            }

        }
    }

    //加载更多
    public void loadMore(List<T> list){
        if (list != null && list.size() > 0){

            int size = list.size();
            int begin = mDatas.size();

            for (int i = 0; i < size; i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i + begin  );
            }
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(mLayoutResId,parent,false);

        return new BaseViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        T t = getItem(position);
        bindData((H) holder,t);
    }

    //绑定数据
    public abstract void bindData(H holder, T t);

    public T getItem(int position) {
        if (position >= mDatas.size())
            return null;
        return mDatas.get(position);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    //显示商品详情
    public void showDetail(Wares wares){

        Intent intent = new Intent(mContext, WaresDetailsActivity.class);

        intent.putExtra(Constants.WARES,wares);

        mContext.startActivity(intent);
    }

}
