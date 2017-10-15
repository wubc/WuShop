package com.example.wushop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * BaseViewHolder封装
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private SparseArray<View> views;
    private BaseAdapter.OnItemClickListenner mListener;


    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListenner listener) {
        super(itemView);

        views = new SparseArray<>();
        this.mListener = listener;
        itemView.setOnClickListener(this);
    }

    public TextView getTextView(int id){
        return findView(id);
    }

    public ImageView getImageView(int id) {

        return findView(id);
    }

    public Button getButton(int id){
        return findView(id);
    }

    public View getView(int id){
        return findView(id);
    }

    private <T extends View> T findView(int id){
        View view = views.get(id);
        if (view == null){
            view = itemView.findViewById(id);
            views.put(id,view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View view) {

        if (mListener != null){
            mListener.onItemClick(view,getLayoutPosition());
        }
    }
}
