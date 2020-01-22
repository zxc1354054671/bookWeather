package com.bw.bookweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zxc on 2020/1/11.
 */

public class Forecast {
    public  String date;
    @SerializedName("tmp")
    public  Temperature temperature;
    @SerializedName("cond")
    public  More more;

    public class Temperature {
        public  String min;
        public  String max;
    }
    public class More {
        @SerializedName("txt_d")
        public String info;
    }
}
