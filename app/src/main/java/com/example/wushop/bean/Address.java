package com.example.wushop.bean;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * 地址信息类
 */

public class Address implements Serializable,Comparable<Address>{

    private Long id;
    private Long userId;
    private String consignee;//收货人
    private String phone;
    private String addr;
    private String zip_code;
    private Boolean isDefault;

    public Address(){

    }

    //地址排序
    @Override
    public int compareTo(@NonNull Address address) {
        if (address.getIsDefault() != null && this.getIsDefault() != null){
            return address.getIsDefault().compareTo(this.getIsDefault());
        }

        return -1;

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
