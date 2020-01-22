package com.bw.bookweather.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zxc on 2020/1/11.
 */

public class WeatherJson {
    @SerializedName("data")
    public  WeatherDataJson weatherDataJson;
    @SerializedName("cityInfo")
    public CityJson cityJson;
    public String status;

}
