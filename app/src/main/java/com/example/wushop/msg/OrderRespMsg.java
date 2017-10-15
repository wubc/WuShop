package com.example.wushop.msg;

import com.example.wushop.bean.Charge;

/**
 * 订单数据返回
 */

public class OrderRespMsg {
    private String orderNum;
    private Charge charge;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }
}

