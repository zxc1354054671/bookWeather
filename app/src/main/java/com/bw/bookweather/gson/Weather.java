package com.bw.bookweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zxc on 2020/1/11.
 */

public class Weather {
    public  String status;
    public  Basic basic;
    public  AQI aqi;
    public  Now now;
    public  Suggestion suggestion;
    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
