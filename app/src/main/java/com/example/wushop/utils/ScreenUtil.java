package com.example.wushop.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕工具类
 */

public class ScreenUtil {

    //获取屏幕宽度
    public static int getScreenWidth(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }

    //获取屏幕高度
    public static int getScreenHeight(Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        return metrics.heightPixels;

    }

    //获取屏幕密度
    public static Point getScrenn(Activity activity){

        Point point = new Point();
        point.x = getScreenWidth(activity);
        point.y = getScreenHeight(activity);
        return point;
    }



}
