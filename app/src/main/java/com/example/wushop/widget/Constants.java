package com.example.wushop.widget;

import com.example.wushop.net.RetrofitAPI;

/**
 * 常量
 */

public class Constants {
    public static final String GOODS_ID = "campaign_id";
    public static final String WARES = "wares";
    public static final String DES_KEY = "Cniao5_123456";
    public static final String USER_JSON = "user_json";
    public static final String TOKEN = "token";
    public static final int REQUEST_CODE = 0;
    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int SUCCESS = 1;
    public static final int FAIL = -1;
    public static final int CANCEL = -2;
    public static final int INVALID = 0;

    public static final int ADDRESS_ADD = 100;
    public static final int ADDRESS_EDIT = 200;


    public static final int TAG_SAVE = 1;
    public static final int TAG_COMPLETE = 2;

    public static final int CART = 1;
    public static final int ORDER = 2;

    public static class API{
        public static final String BASE_URL = "http://112.124.22.238:8081/course_api/";
        public static final String WARES_DETAILS = BASE_URL + "wares/detail.html";

    }
}
