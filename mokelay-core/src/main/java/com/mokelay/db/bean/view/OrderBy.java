package com.mokelay.db.bean.view;

import com.mokelay.db.bean.constant.Order;

/**
 * Order By
 *
 * Author :CarlChen
 * Date:17/7/20
 */
public class OrderBy {
    private String orderFieldName;
    private Order order=Order.ASC;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getOrderFieldName() {
        return orderFieldName;
    }

    public void setOrderFieldName(String orderFieldName) {
        this.orderFieldName = orderFieldName;
    }
}
