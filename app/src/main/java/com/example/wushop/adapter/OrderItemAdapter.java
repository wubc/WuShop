package com.example.wushop.adapter;

import android.content.Context;

import com.example.wushop.R;
import com.example.wushop.bean.OrderItem;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/10.
 */

public class OrderItemAdapter extends SimpleAdapter<OrderItem> {

    public OrderItemAdapter(Context context, List<OrderItem> datas) {
        super(context, datas, R.layout.template_order_wares);
    }

    @Override
    public void bindData(BaseViewHolder holder, OrderItem item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(item.getWares().getImgUrl());
    }

    public float getTotalPrice(){
        float sum = 0;
        if (!isNull()){
            return sum;
        }

        for (OrderItem item : mDatas){
                sum += Float.parseFloat(item.getWares().getPrice());
        }

        return sum;
    }

    private boolean isNull(){
        return ( mDatas != null && mDatas.size() > 0);
    }
}
