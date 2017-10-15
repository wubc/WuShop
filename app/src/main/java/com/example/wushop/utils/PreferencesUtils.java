package com.example.wushop.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PreferencesUtils类
 */

public class PreferencesUtils {

    public static String PREFRENCE_NAME = "Wu_Pref_Common";

    public static boolean putString(Context context, String key, String value){

        SharedPreferences settings = context.getSharedPreferences(PREFRENCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key,value);

        return editor.commit();

    }

    public static String getString(Context context,String key){
        return getString(context, key, null);
    }

    public static String getString(Context context, String key, String defaultValue){
        SharedPreferences settings = context.getSharedPreferences(PREFRENCE_NAME,Context.MODE_PRIVATE);
        return settings.getString(key,defaultValue);
    }

}
