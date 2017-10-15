package com.example.wushop.msg;

/**
 * 响应基类
 */

public class BaseResMsg {
    public final static int STATUS_SUCCESS =  1;
    public final static int STATUS_ERROR = 2;
    public static final String MSG_SUCCESS = "seccess";

    protected int status = STATUS_SUCCESS;
    protected String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

