package com.example.wushop.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.wushop.R;

/**
 * 带清除按钮的自定义TextView
 */

public class ClearEditText extends AppCompatEditText implements View.OnFocusChangeListener,View.OnTouchListener,TextWatcher{

    private Drawable clearDrawable;

    private boolean hasFoucs;


    public ClearEditText(Context context) {
        this(context,null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        clearDrawable = getCompoundDrawables()[2];
        if (clearDrawable == null) {
            clearDrawable = getResources().getDrawable(R.drawable.icon_delete_20);

        }
        clearDrawable.setBounds(0,0,clearDrawable.getIntrinsicWidth(),clearDrawable.getIntrinsicHeight());

        //默认隐藏图标
        setClearIconVisible(false);
        //焦点改变的监听
        setOnFocusChangeListener(this);
        //输入框里面内容发生改变的监听
        addTextChangedListener(this);


    }

    //删除图标的隐藏和显示
    private void setClearIconVisible(boolean b) {
        Drawable drawable = b ? clearDrawable:null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1],
                drawable,
                getCompoundDrawables()[3]);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            int x = (int) event.getX();//获取触摸点x坐标
            int y = (int) event.getY();//获取触摸点y坐标
            Rect rect = clearDrawable.getBounds();//清除图标矩形区域
            int height = rect.height();//清楚图标高度
            int distance = (getHeight()-height)/2;//清除图标顶部到控件顶部的距离
            boolean isInnerWidth = x > (getWidth()-getTotalPaddingRight()) && x < (getWidth()-getPaddingRight());//判断触摸是否在清楚图标宽内
            boolean isInnerHeight = y > distance && y < distance+height;//判断触摸是否在清除图标高度内
            if (isInnerWidth && isInnerHeight){
                this.setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
            }
            return true;
        }
        return false;
    }

    /**
     * 当ClearEditText焦点发生变化的时候，
     * 输入长度为零，隐藏删除图标，否则，显示删除图标
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        this.hasFoucs = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence sequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence sequence, int i, int i1, int i2) {

        if (hasFoucs) {
            setClearIconVisible(sequence.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
