package com.example.wushop.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.wushop.bean.User;
import com.example.wushop.widget.Constants;

/**
 * 用户管理类
 */

public class UserLocalData {

    //存储用户数据
    public static void putUser(Context context, User user){

        String user_json = JSONUtil.toJson(user);
        PreferencesUtils.putString(context, Constants.USER_JSON, user_json);
    }

    //存储Token
    public static void putToken(Context context, String token){
        PreferencesUtils.putString(context, Constants.TOKEN, token);
    }

    //获取用户数据
    public static User getUser(Context context){
        String user_data = PreferencesUtils.getString(context, Constants.USER_JSON);
        if (!TextUtils.isEmpty(user_data)) {
            return  JSONUtil.fromJson(user_data, User.class);
        }
        return null;
    }

    //获取Token
    public static String getToken(Context context){
        String token = PreferencesUtils.getString(context, Constants.TOKEN);
        return token;
    }

    //清除user
    public static void clearUser(Context context){
        PreferencesUtils.putString(context, Constants.USER_JSON, "");
    }

    //清除Token
    public static void clearToken(Context context){
        PreferencesUtils.putString(context, Constants.TOKEN, "");
    }
}
