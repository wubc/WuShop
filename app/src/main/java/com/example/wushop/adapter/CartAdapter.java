package com.example.wushop.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


import com.example.wushop.R;
import com.example.wushop.bean.ShoppingCart;
import com.example.wushop.utils.CartOperator;
import com.example.wushop.widget.NumberAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 购物车
 */

public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListenner{

    private CheckBox mCheckBox;
    private TextView mTextView;

    private CartOperator mCartOperator;
    public CartAdapter(Context context, List<ShoppingCart> datas, CheckBox checkBox, TextView textView){
        super(context,datas, R.layout.template_cart);
        this.mCheckBox = checkBox;
        this.mTextView = textView;

        mCartOperator = CartOperator.getInstance(context);

        setCheckBox(checkBox);
        setTextView(textView);

        setOnItemClickListenner(this);

        showTotalPrice();

    }

    private void setTextView(TextView view) {
        this.mTextView = view;
    }

    private void setCheckBox(CheckBox box) {
        this.mCheckBox = box;

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAll_None(mCheckBox.isChecked());
                showTotalPrice();
            }
        });
    }

    public void checkAll_None(boolean isChecked){
        if (!isNull()){
            return;
        }

        int i = 0;

        for (ShoppingCart cart:mDatas){
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }

    public void del_cart(){
        if (!isNull()){//购物栏为空
            return;
        }
        //list长度会改变，不能使用foreach循环，使用迭代器实现遍历
        for (Iterator iterator = mDatas.iterator();iterator.hasNext();){
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()) {
                int index = mDatas.indexOf(cart);
                mCartOperator.delete(cart);
                iterator.remove();
                notifyItemRemoved(index);
            }
        }
    }

    private boolean isNull() {
        return (mDatas != null && mDatas.size() > 0);
    }

    public void showTotalPrice(){
        float total  = getTotalPrice();

        mTextView.setText(
                Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"),
                TextView.BufferType.SPANNABLE
        );
    }

    private float getTotalPrice() {
        float sum = 0;

        if (!isNull()){
            return sum;
        }

        for (ShoppingCart cart:mDatas){
            if (cart.isChecked()){
                sum += cart.getCount() * Float.parseFloat(cart.getPrice());
            }
        }
        return sum;
    }

    @Override
    public void onItemClick(View view, int positon) {
        ShoppingCart cart = getItem(positon);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(positon);

        //不是全选要改变checkbox的状态
        checkListen();
        showTotalPrice();
    }

    public List<ShoppingCart> getCheckData() {
        List<ShoppingCart> temp = new ArrayList<>();
        for (ShoppingCart cart : mDatas){
            if (cart.isChecked()){
                temp.add(cart);
            }
        }

        return temp;
    }

    private void checkListen() {
        int count = 0;
        int checkNum = 0;

        if (mDatas !=null){
            count = mDatas.size();

            for (ShoppingCart cart:mDatas){
                if (!cart.isChecked()){
                    mCheckBox.setChecked(false);
                    break;
                }else {
                    checkNum +=1;
                }
            }

            if (count == checkNum){
                mCheckBox.setChecked(true);
            }
        }
    }

    @Override
    public void bindData(BaseViewHolder holder, final ShoppingCart item) {

        holder.getTextView(R.id.tv_title_cart).setText(item.getName());
        holder.getTextView(R.id.tv_price_cart).setText("￥ " + item.getPrice());

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view_cart);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox = (CheckBox) holder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());

        NumberAddSubView numberAddSubView = (NumberAddSubView) holder.getView(R.id.add_sub_view);
        numberAddSubView.setValue(item.getCount());

        numberAddSubView.setOnButonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClickListener(View view, int value) {
                item.setCount(value);
                mCartOperator.update(item);
                showTotalPrice();
            }

            @Override
            public void onButtonSubClickListener(View view, int value) {
                item.setCount(value);
                mCartOperator.update(item);
                showTotalPrice();
            }
        });
    }
}
