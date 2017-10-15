package com.example.wushop.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wushop.R;

/**
 * Created by Administrator on 2017/9/6.
 */

public class ShopToolbar extends Toolbar {

    private LayoutInflater mInflater;
    private View view;
    private EditText mEditText;
    private TextView mTextView;
    private Button mButton;

    private ImageButton mLeftButton;
    private ImageButton mRightImgButton;

    public ShopToolbar(Context context) {
        this(context,null);
    }

    public ShopToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShopToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化控件
        initView();

        setContentInsetsRelative(10,10);

        if (attrs != null) {
            //读取自定义属性
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.ShopToolbar, defStyleAttr, 0);

            //搜索框
            boolean isShowSerachView = a.getBoolean(R.styleable.ShopToolbar_isShowSearchView,false);
            if (isShowSerachView){
                showSearchView();
                hideTextView();
                hideRightButton();
                hideRightImgButton();
                hideLeftButton();
            }else {
                hideSearchView();
                showTextView();
                showRightButton();
                showRightImgButton();
                showLeftButton();
            }

            //按钮文字
            CharSequence charSequence = a.getText(R.styleable.ShopToolbar_rightButtonText);
            if (charSequence != null){
                setRightButtonText(charSequence);
            }

            a.recycle();
        }

    }

    private void initView() {

        if(view == null) {
            view = mInflater.from(getContext()).inflate(R.layout.toolbar, null);
            mEditText = (EditText) view.findViewById(R.id.toolbar_searchview);
            mTextView = (TextView) view.findViewById(R.id.toolbar_textview);
            mButton = (Button) view.findViewById(R.id.toolbar_right_Button);

            mLeftButton = (ImageButton) view.findViewById(R.id.toolbar_left_button);
            mRightImgButton = (ImageButton) view.findViewById(R.id.toolbar_right_imgbutton);

            mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        mEditText.setHint("");
                    }
                }
            });
            mEditText.setHint("请输入搜索内容");

            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(view, layoutParams);
        }
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        super.setTitle(title);
        initView();

        mTextView.setText(title);
        showTextView();

    }

    public void showTextView(){
        if (mTextView != null ) {
            mTextView.setVisibility(VISIBLE);
        }
    }

    public void hideTextView(){
        if (mTextView != null) {
            mTextView.setVisibility(GONE);
        }
    }

    public void showSearchView(){
        if (mEditText != null){
            mEditText.setVisibility(VISIBLE);
        }
    }

    public void hideSearchView(){
        if (mEditText != null){
            mEditText.setVisibility(GONE);
        }
    }

    public void showRightButton(){
        if (mButton !=null){
            mButton.setVisibility(VISIBLE);
        }
    }

    public void hideRightButton(){
        if(mButton != null){
            mButton.setVisibility(GONE);
        }
    }

    //显示右边图片按钮
    public void showRightImgButton() {
        if (mRightImgButton != null)
            mRightImgButton.setVisibility(VISIBLE);
    }

    //隐藏右边图片按钮
    public void hideRightImgButton() {
        if (mRightImgButton != null)
            mRightImgButton.setVisibility(GONE);

    }

    //显示左边按钮
    public void showLeftButton() {
        if (mLeftButton != null)
            mLeftButton.setVisibility(VISIBLE);
    }

    //隐藏左边按钮
    public void hideLeftButton() {
        if (mLeftButton != null)
            mLeftButton.setVisibility(GONE);

    }

    //获取右边按钮
    public Button getRightButton(){
        return this.mButton;
    }

    //获取右边按钮
    public ImageButton getRightImgButton(){
        return this.mRightImgButton;
    }

    public void setRightButtonText(int resId){
        setRightButtonText(getResources().getString(resId));
    }

    //设置按钮文字
    public void setRightButtonText(CharSequence text) {
        showRightButton();
        mButton.setText(text);
    }

    public void setleftButtonIcon(Drawable icon) {
        if (icon != null) {
            mLeftButton.setImageDrawable(icon);
            mLeftButton.setVisibility(VISIBLE);
        }
    }

    public void setleftButtonIcon(int id) {
        setleftButtonIcon(getResources().getDrawable(id));
    }

    public void setRightImgButtonIcon(int id){
        setRightImgButtonIcon(getResources().getDrawable(id));
    }

    private void setRightImgButtonIcon(Drawable icon) {
        if (icon != null){
            mRightImgButton.setImageDrawable(icon);
            showRightButton();
        }
    }

    public void setLeftButtonOnClickListener(OnClickListener listener){
        if (mLeftButton != null){
            mLeftButton.setOnClickListener(listener);
        }
    }

    public void setRightButtonOnClickListener(OnClickListener listener){
        if (mButton != null){

            mButton.setOnClickListener(listener);
        }
    }


    public void setRightButtonImgOnClickListener(OnClickListener listener){
        if (mRightImgButton != null){
            mRightImgButton.setOnClickListener(listener);
        }
    }

}
