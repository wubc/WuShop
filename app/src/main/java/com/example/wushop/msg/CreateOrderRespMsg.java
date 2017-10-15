package com.example.wushop.msg;

/**
 * 创建订单返回信息
 */

public class CreateOrderRespMsg extends BaseResMsg{
    private OrderRespMsg data;

    public OrderRespMsg getData() {
        return data;
    }

    public void setData(OrderRespMsg data) {
        this.data = data;
    }
}
