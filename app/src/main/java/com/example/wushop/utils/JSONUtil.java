package com.example.wushop.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Json工具类
 */

public class JSONUtil {

    private static Gson mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH-mm-ss").create();
    public static Gson getGson(){
        return mGson;
    }

    public static <T>T fromJson(String json, Class<T> tClass){
        return mGson.fromJson(json,tClass);
    }

    public static <T>T fromJson(String json, Type type){
        return mGson.fromJson(json,type);
    }

    public static String toJson(Object object){
        return mGson.toJson(object);
    }

}
