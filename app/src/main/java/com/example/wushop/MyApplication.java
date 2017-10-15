package com.example.wushop;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.wushop.bean.User;
import com.example.wushop.utils.PreferencesUtils;
import com.example.wushop.utils.UserLocalData;
import com.example.wushop.widget.Constants;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * 单例模式，懒加载
 */

public class MyApplication extends Application {

    private static MyApplication mApplication;
    private User user;

    public static MyApplication getmApplication(){
        if (mApplication == null){
            mApplication = new MyApplication();
        }
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        Fresco.initialize(this);
    }

    private void initUser(){
        user = UserLocalData.getUser(this);
    }

    //获取user
    public User getUser(){
        return user;
    }

    //获取token
    public String getToken(){
        return UserLocalData.getToken(this);
    }

    //保存用户信息和token到本地
    public void putUser(User user, String token){
        this.user = user;
        UserLocalData.putUser(this, user);
        UserLocalData.putToken(this, token);
    }

    //清除用户信息和token
    public void clearUser(){
        this.user = null;
        UserLocalData.clearUser(this);
        UserLocalData.clearToken(this);
    }

    private Intent mIntent;

    //保存登录意图
    public void putIntent(Intent intent){
        this.mIntent = intent;
    }

    //获取登录意图
    public Intent getIntent(){
        return mIntent;
    }

}
