package com.example.wushop.msg;

/**
 * 登录返回信息
 */

public class LoginRespMsg<T> extends BaseResMsg{
    public String token;
    public T data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
