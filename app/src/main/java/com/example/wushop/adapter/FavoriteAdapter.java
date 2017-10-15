package com.example.wushop.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.example.wushop.R;
import com.example.wushop.bean.Favorite;
import com.example.wushop.bean.Wares;
import com.example.wushop.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017/10/6.
 */

public class FavoriteAdapter extends SimpleAdapter<Favorite> {

    private FavoriteLisneter mFavoriteLisneter;

    public FavoriteAdapter(Context context, List<Favorite> data, FavoriteLisneter lisneter){
        super(context, data, R.layout.template_favorite_item);
        this.mFavoriteLisneter = lisneter;

    }

    @Override
    public void bindData(BaseViewHolder holder, final Favorite favorite) {
        Wares wares = favorite.getWares();

        holder.getTextView(R.id.tv_title).setText(wares.getName());
        holder.getTextView(R.id.tv_price).setText(wares.getPrice());

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        Button buttonRemove = holder.getButton(R.id.btn_remove);
        Button buttonLike = holder.getButton(R.id.btn_like);

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFavoriteLisneter != null){
                    mFavoriteLisneter.onClickDelete(favorite);
                }
            }
        });
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(mContext, "功能正在完善...");
            }
        });

    }

    public interface FavoriteLisneter{
        void onClickDelete(Favorite favorite);
    }

}
