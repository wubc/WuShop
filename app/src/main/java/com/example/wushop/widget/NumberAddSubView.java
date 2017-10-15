package com.example.wushop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wushop.R;

/**
 * Created by Administrator on 2017/9/28.
 */

public class NumberAddSubView extends LinearLayout implements View.OnClickListener{

    private Button addButton;
    private Button subButton;
    private TextView countTV;

    private OnButtonClickListener mOnButtonClickListener;


    private LayoutInflater mInflater;

    private int minValue = 1;
    private int maxValue;
    private int value;

    public NumberAddSubView(Context context) {
        this(context,null);
    }

    public NumberAddSubView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NumberAddSubView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        initView();

        if (attrs != null){
            TintTypedArray array = TintTypedArray.obtainStyledAttributes(context,
                    attrs,
                    R.styleable.NumberAddSubView,
                    defStyleAttr,
                    0);

            //value
            int value = array.getInt(R.styleable.NumberAddSubView_value,0);
            setValue(value);

            //mixValue
            int mixValue = array.getInt(R.styleable.NumberAddSubView_minValue,0);
            setMixValue(mixValue);

            //maxValue
            int maxValue = array.getInt(R.styleable.NumberAddSubView_maxValue,0);
            setMaxValue(maxValue);

            Drawable btnAdd = array.getDrawable(R.styleable.NumberAddSubView_btnAddBackground);
            Drawable btnSub = array.getDrawable(R.styleable.NumberAddSubView_btnSubBackground);
            Drawable textView = array.getDrawable(R.styleable.NumberAddSubView_textViewBackground);

            //设置控件背景
            setBtnAddBackground(btnAdd);
            setBtnSubBackground(btnSub);
            setTextViewBackground(textView);

            array.recycle();
        }


    }

    //加
    private void numAdd(){
        if (value < maxValue){
            value += 1;
        }
        countTV.setText(value + "");
    }

    //减
    private void numSub(){
        if (value > minValue){
            value -= 1;
        }
        countTV.setText(value + "");
    }

    //获取值
    public int getValue(){
        String val = countTV.getText().toString();
        //判断是否为数字
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        if (val != null && !"".equals(val) && val.matches(regex))
            this.value = Integer.parseInt(val);
        return value;
    }

    public void setValue(int value) {
        countTV.setText(value+"");
        this.value = value;
    }

    private void setMaxValue(int value) {
        this.maxValue = value;
    }

    private void setMixValue(int value) {
        this.minValue = value;
    }

    private void setTextViewBackground(Drawable view) {
        countTV.setBackground(view);
    }

    private void setBtnSubBackground(Drawable sub) {
        subButton.setBackground(sub);
    }

    private void setBtnAddBackground(Drawable add) {
        addButton.setBackground(add);
    }



    private void initView() {
        View view = mInflater.inflate(R.layout.widget_number_add_sub,this,true);
        addButton = (Button) view.findViewById(R.id.btn_add);
        subButton = (Button) view.findViewById(R.id.btn_sub);
        countTV = (TextView) view.findViewById(R.id.tv_num);
        countTV.setInputType(InputType.TYPE_NULL);
        countTV.setKeyListener(null);

        addButton.setOnClickListener(this);
        subButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add){
            numAdd();

            if (mOnButtonClickListener != null){
                mOnButtonClickListener.onButtonAddClickListener(view,value);
            }
        }else if (view.getId() == R.id.btn_sub){
            numSub();
            if (mOnButtonClickListener != null){
                mOnButtonClickListener.onButtonSubClickListener(view,value);
            }
        }
    }

    public void setOnButonClickListener(OnButtonClickListener listener){
        this.mOnButtonClickListener = listener;
    }


    //按钮监听接口
    public interface OnButtonClickListener{

        void onButtonAddClickListener(View view, int value);

        void onButtonSubClickListener(View view, int value);
    }
}
