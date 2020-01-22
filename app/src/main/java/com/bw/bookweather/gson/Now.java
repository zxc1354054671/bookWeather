package com.bw.bookweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zxc on 2020/1/11.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;
    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }
}
