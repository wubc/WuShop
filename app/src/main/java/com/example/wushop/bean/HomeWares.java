package com.example.wushop.bean;

import java.io.Serializable;

/**
 * 首页三个大图数据
 */

public class HomeWares implements Serializable{

    private long id;
    private String title;
    private Goods cpOne;
    private Goods cpTwo;
    private Goods cpThree;

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

    public Goods getCpOne() {
        return cpOne;
    }

    public void setCpOne(Goods cpOne) {
        this.cpOne = cpOne;
    }

    public Goods getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Goods cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Goods getCpThree() {
        return cpThree;
    }

    public void setCpThree(Goods cpThree) {
        this.cpThree = cpThree;
    }
}
