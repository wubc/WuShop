package com.example.wushop.bean;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2017/9/5.
 */

public class Tab {

    private int title;
    private int image;
    private Class mFragment;

    public Tab(int image, int title, Class fragment) {
        this.image = image;
        this.title = title;
        mFragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Class getFragment() {
        return mFragment;
    }

    public void setFragment(Class fragment) {
        mFragment = fragment;
    }
}
