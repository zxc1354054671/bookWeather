package com.bw.bookweather.db;

/**
 * 县
 * Created by zxc on 2020/1/11.
 */

public class County extends EntityModel {
    private String parentId;
//    private String countyCode;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

//    public String getWeatherId() {
//        return countyCode;
//    }
//
//    public void setWeatherId(String countyCode) {
//        this.countyCode = countyCode;
//    }
}
