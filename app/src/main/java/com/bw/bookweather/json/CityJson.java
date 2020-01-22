package com.bw.bookweather.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zxc on 2020/1/11.
 */

public class CityJson {
    @SerializedName("city")
    public  String cityName;
    @SerializedName("citykey")
    public  String code;

    public String updateTime;
}
