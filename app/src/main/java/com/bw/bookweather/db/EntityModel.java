package com.bw.bookweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by zxc on 2020/1/11.
 */

public class EntityModel extends DataSupport {
    private String id;
    private String name;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
