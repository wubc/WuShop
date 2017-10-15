package com.example.wushop.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.wushop.R;
import com.example.wushop.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18.
 */

public class CategoryWaresAdapter extends SimpleAdapter<Wares> {
    public CategoryWaresAdapter(Context context, List<Wares> datas) {
        super(context, datas,  R.layout.template_grid_wares);
    }

    @Override
    public void bindData(BaseViewHolder holder, Wares wares) {
        holder.getTextView(R.id.tv_title).setText(wares.getName());
        holder.getTextView(R.id.tv_price).setText("￥ " + wares.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
    }
}
