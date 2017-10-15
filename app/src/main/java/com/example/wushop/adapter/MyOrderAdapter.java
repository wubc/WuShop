package com.example.wushop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wushop.R;
import com.example.wushop.bean.Order;
import com.example.wushop.bean.OrderItem;
import com.example.wushop.utils.ScreenUtil;
import com.example.wushop.utils.ToastUtils;
import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;

import java.util.List;

/**
 * 我的订单适配器
 */

public class MyOrderAdapter extends SimpleAdapter<Order> {

    private OnItemWaresClickListener mItemWaresClickListener;

    public MyOrderAdapter(Context context, List<Order> datas, OnItemWaresClickListener listener) {
        super(context, datas, R.layout.template_my_orders);
        this.mItemWaresClickListener = listener;
    }


    @Override
    public void bindData(BaseViewHolder holder, final Order order) {

        holder.getTextView(R.id.tv_order_num).setText("订单号："+order.getOrderNum());
        holder.getTextView(R.id.tv_order_money).setText("实付金额："+order.getAmount());

        TextView tv_status = holder.getTextView(R.id.tv_status);

        Button btn_buyMore = (Button) holder.getTextView(R.id.btn_buy_more);
        Button btn_comment = (Button) holder.getTextView(R.id.btn_comment);

        //根据订单状态分别显示订单
        switch (order.getStatus()){

            case Order.STATUS_SUCCESS:
                tv_status.setText("支付成功");
                tv_status.setTextColor(Color.parseColor("#ff4CAF50"));
                break;

             case Order.STATUS_PAY_FAIL:
                 tv_status.setText("支付失败");
                 tv_status.setTextColor(Color.parseColor("#ffF44336"));
                 break;

            case Order.STATUS_PAY_WAIT:
                tv_status.setText("等待支付");
                tv_status.setTextColor(Color.parseColor("#ffFFEB3B"));
                break;
        }

        btn_buyMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemWaresClickListener.onItemWaresClickListener(view,order);
            }
        });

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(mContext,"功能正在完善...");
            }
        });

        NineGridlayout nineGridlayout = (NineGridlayout) holder.getView(R.id.iv_ngrid_layout);
        nineGridlayout.setGap(10);//设置图片间隔
        nineGridlayout.setDefaultWidth(ScreenUtil.getScreenWidth(mContext) / 4);
        nineGridlayout.setDefaultHeight(ScreenUtil.getScreenHeight(mContext) / 4);
        nineGridlayout.setAdapter(new OrderItemAdapter(mContext, order.getItems()));

    }

    public interface OnItemWaresClickListener{
        void onItemWaresClickListener(View view, Order order);
    }

    class OrderItemAdapter extends NineGridAdapter{

        private List<OrderItem> mItems;

        public OrderItemAdapter(Context context, List<OrderItem> items) {
            super(context, items);
            this.mItems = items;
        }

        @Override
        public int getCount() {
            return (mItems == null) ? 0 : mItems.size();
        }

        @Override
        public String getUrl(int positon) {
            OrderItem item = mItems.get(positon);
            return (item == null) ? null : item.getWares().getImgUrl();
        }

        @Override
        public OrderItem getItem(int position) {
            return (mItems == null) ? null : mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            OrderItem item = mItems.get(position);
            return item == null ? null : item.getOrderId();
        }

        @Override
        public View getView(int i, View view) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(imageView);
            return imageView;
        }
    }
}
