package com.example.wushop.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.wushop.R;
import com.example.wushop.bean.Banner;
import com.example.wushop.bean.Goods;
import com.example.wushop.bean.HomeWares;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * 主页商品适配器
 */

public class HomeWaresAdapter extends RecyclerView.Adapter<HomeWaresAdapter.Viewholder> {

    private Context context;
    private View mView;
    private List<Banner> mBannerList;
    private List<HomeWares> mWaresList;

    private int layoutId;//加载的布局id
    private static int VIEW_TYPE_L = 1;//item布局方式：大图片在左边
    private static int VIEW_TYPE_R = 2;//item布局方式：大图片在右边

    private GoodsClickListener mGoodsClickListener;
    public HomeWaresAdapter(Context context){
        this.context = context;
    }

    //通过set方法把数据Banner、wares传进来，而不是通过构造参数，因为数据不是唯一类型
    public void setBanner(List<Banner> banners){
        this.mBannerList = banners;
        notifyItemChanged(0);//刷新第一个item
        Log.e("setBanner",banners.size()+"");
    }
    public void setWares(List<HomeWares> homeWares){
        this.mWaresList = homeWares;
        notifyItemRangeChanged(1,homeWares.size());
    }



    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0){
            layoutId = R.layout.layout_slider;
        }else if (viewType == VIEW_TYPE_L){
            layoutId = R.layout.template_home_cardview;
        }else if (viewType == VIEW_TYPE_R){
            layoutId = R.layout.template_home_cardview2;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new Viewholder(view,viewType);
    }

    @Override
    public void onBindViewHolder(Viewholder viewholder, int position) {
        if (position == 0){
            if (mBannerList !=null ){
                for (Banner banner:mBannerList){

                    TextSliderView sliderView = new TextSliderView(context);
                    sliderView.image(banner.getImgUrl());
                    sliderView.description(banner.getName());
                    sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                    viewholder.mSliderLayout.addSlider(sliderView);
                }
            }

                //指示器
                viewholder.mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
                //动画效果
                viewholder.mSliderLayout.setCustomAnimation(new DescriptionAnimation());
                //转场效果
                viewholder.mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
                //时长
                viewholder.mSliderLayout.setDuration(3000);
        }else {

                HomeWares homeWares = mWaresList.get(position - 1);
                viewholder.textTitle.setText(homeWares.getTitle());
                Picasso.with(context).load(homeWares.getCpOne().getImageUrl()).into(viewholder.imageBig);
                Picasso.with(context).load(homeWares.getCpTwo().getImageUrl()).into(viewholder.imageSmallTop);
                Picasso.with(context).load(homeWares.getCpThree().getImageUrl()).into(viewholder.iamgeSmallBottom);

        }

    }


    @Override
    public int getItemCount() {
        return mWaresList == null ? 1 : mWaresList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0){
            return  0;
        }else if ( position%2 == 0){
            return VIEW_TYPE_R;
        }
        return VIEW_TYPE_L;
    }

    class Viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SliderLayout mSliderLayout;

        private TextView textTitle;
        private ImageView imageBig;
        private ImageView imageSmallTop;
        private ImageView iamgeSmallBottom;

        public Viewholder(View itemView,int viewType) {
            super(itemView);

            if (viewType == 0){
                mSliderLayout = (SliderLayout) itemView.findViewById(R.id.slider);
            }else{

                textTitle = (TextView) itemView.findViewById(R.id.text_title);
                imageBig = (ImageView) itemView.findViewById(R.id.imgview_big);
                imageSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
                iamgeSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

                imageBig.setOnClickListener(this);
                imageSmallTop.setOnClickListener(this);
                iamgeSmallBottom.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            if (mGoodsClickListener != null){
                anim(view);
            }
        }

        //图片翻转效果
        private void anim(final View v) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v,"rotationX",0.0F,360F)
                    .setDuration(200);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    HomeWares homeWares = mWaresList.get(getLayoutPosition() - 1);

                    switch (v.getId()){
                        case R.id.imgview_big :
                            mGoodsClickListener.setOnClick(v,homeWares.getCpOne());
                        case R.id.imgview_small_top :
                            mGoodsClickListener.setOnClick(v,homeWares.getCpTwo());
                        case R.id.imgview_small_bottom :
                            mGoodsClickListener.setOnClick(v,homeWares.getCpThree());
                    }

                }
            });

            animator.start();
        }


    }

    public void setOnGoodsClickListener(GoodsClickListener listener){
        this.mGoodsClickListener = listener;
    }

    public interface GoodsClickListener{
        void setOnClick(View view, Goods goods);
    }


}
