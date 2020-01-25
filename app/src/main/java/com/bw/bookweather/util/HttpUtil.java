package com.bw.bookweather.util;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by zxc on 2020/1/11.
 */

public class HttpUtil {
    static Date lastDate;
    static String lastUrl;

    public static void sendRequest(String url, okhttp3.Callback callback) {
//        if (lastDate == null) {
//
//        } else {
//            if (new Date().getTime() - lastDate.getTime() < 5 * 60 * 1000) {
//                return;
//            }
//        }
//        lastDate = new Date();
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(url).build();
        client.newCall(req).enqueue(callback);
    }
}
