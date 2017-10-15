package com.example.wushop.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16.
 */

public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {
    public SimpleAdapter(Context mContext, int mLayoutResId) {
        super(mContext, mLayoutResId);
    }

    public SimpleAdapter(Context context, List<T> datas, int layoutResId) {
        super(context, datas, layoutResId);
    }
}
