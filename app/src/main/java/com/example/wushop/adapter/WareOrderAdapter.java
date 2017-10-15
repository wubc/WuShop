package com.example.wushop.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.wushop.R;
import com.example.wushop.bean.ShoppingCart;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 订单商品适配器
 */

public class WareOrderAdapter extends SimpleAdapter<ShoppingCart> {

    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(context, datas, R.layout.template_order_wares);
    }

    @Override
    public void bindData(BaseViewHolder holder, ShoppingCart cart) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(cart.getImgUrl()));
    }

    public float getTotalPrice(){
        float sum  = 0;
        if (!isNull()){
            return sum;
        }

        for (ShoppingCart cart : mDatas){
            if (cart.isChecked()){
                sum += cart.getCount() * Float.parseFloat(cart.getPrice());
            }
        }
        return sum;
    }

    private boolean isNull(){
        return ( mDatas != null && mDatas.size() > 0 );
    }
}
