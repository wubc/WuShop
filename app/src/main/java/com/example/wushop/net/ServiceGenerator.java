package com.example.wushop.net;

import android.app.Activity;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/9/7.
 */

public class ServiceGenerator {

    private static RetrofitAPI mAPI;
    private static Activity mContext;

    public static RetrofitAPI getRetrofit(Activity context){

        mContext = context;

        if (mAPI == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitAPI.BASE_URL)
                    .client(getOkhttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            mAPI = retrofit.create(RetrofitAPI.class);

        }

        return mAPI;
    }

    private static OkHttpClient getOkhttpClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(
                        new BaseInterceptor.Builder()
                        .addHeaderParamsMap(getHeaderMap())
                        .build())
                .build();
        return okHttpClient;
    }

    private static Map<String, String> getHeaderMap() {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json; charset=UTF-8");
        map.put("Connection", "application/json; charset=UTF-8");
        map.put("Accept", "*/*");
        return map;
    }

}
