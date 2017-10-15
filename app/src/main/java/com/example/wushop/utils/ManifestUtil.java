package com.example.wushop.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Manifest工具类
 */

public class ManifestUtil {
    public static String getMetaDataValue(Context context, String name){

        Object value = null;
        PackageManager manager = context.getPackageManager();
        ApplicationInfo info;

        try {
            info = manager.getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null){
                value = info.metaData.get(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not read the name in the manifest file.",e);
        }

        if (value == null){
            throw new RuntimeException("the name "+name+"' is not defined in the manifest file's meta data.'");
        }
        return value.toString();
    }
}
