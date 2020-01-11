package com.bw.bookweather.db;

/**
 * Created by zxc on 2020/1/11.
 */

public class City extends EntityModel {
    private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
