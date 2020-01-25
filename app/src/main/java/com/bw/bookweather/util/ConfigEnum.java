package com.bw.bookweather.util;

/**
 * Created by zxc on 2020/1/15.
 */

public enum ConfigEnum {
    LOCATION("location"),TYPE_CONFIG("raw/config");

    private String value;

    ConfigEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
