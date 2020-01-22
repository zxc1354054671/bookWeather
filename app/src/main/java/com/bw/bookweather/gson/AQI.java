package com.bw.bookweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zxc on 2020/1/11.
 */

public class AQI {
    public  AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
