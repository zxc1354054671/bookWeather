package com.bw.bookweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by zxc on 2020/1/11.
 */

public class HttpUtil {
    public static void sendRequest(String url,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(url).build();
        client.newCall(req).enqueue(callback);
    }
}
