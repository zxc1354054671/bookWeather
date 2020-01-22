package com.bw.bookweather;

import android.util.Log;

import com.bw.bookweather.json.WeatherJson;
import com.bw.bookweather.util.HttpUtil;
import com.bw.bookweather.util.Utility;

import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class T1 {
    @Test
    public void t() throws Exception {
        System.out.println(1);
        String countyCode = "101010300";
        String url = "http://t.weather.sojson.com/api/weather/city/" + countyCode;
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
//
//                        swipeRefresh.setRefreshing(false);
//                    }
//                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String address = response.body().string();
                Log.d(TAG, "onResponse: "+address);
                final WeatherJson weatherJson = Utility.handleWeatherJsonResponse(address);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (weather != null && "ok".equals(weather.status)) {
//                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
//                            edit.putString("weather", address);
//                            edit.apply();
//                            showWeatherInfo(weather);
//                        } else {
//                            Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
//                        }
//                        swipeRefresh.setRefreshing(false);
//                    }
//                });
            }
        });

    }
}