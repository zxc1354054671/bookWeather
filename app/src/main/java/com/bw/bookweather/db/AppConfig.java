package com.bw.bookweather.db;

/**
 * Created by zxc on 2020/1/11.
 */

public class AppConfig extends EntityModel {
    private String value;
    private String type;
    private Integer orderId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
