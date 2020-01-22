package com.bw.bookweather.json;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zxc on 2020/1/11.
 */

public class WeatherDataJson {
    public  String shidu;
    public  String pm25;
    public  String pm10;
    public  String quality;
    public  String wendu;
    public  String ganmao;
    @SerializedName("forecast")
    public List<ForecastJson> forecastJsonList;


}
