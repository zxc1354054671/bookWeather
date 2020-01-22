package com.bw.bookweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zxc on 2020/1/11.
 */

public class Basic {
    @SerializedName("city")
    public  String cityName;
    @SerializedName("id")
    public  String countyCode;

    public Update update;


    public class Update {
        @SerializedName("loc")
        public  String updateTime;
    }
}
