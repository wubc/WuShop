package com.example.wushop.bean;

import java.io.Serializable;

/**
 * 首页图片item数据
 */

public class Goods implements Serializable{

    private long id;
    private String title;
    private String imgUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imgUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imgUrl = imageUrl;
    }
}
