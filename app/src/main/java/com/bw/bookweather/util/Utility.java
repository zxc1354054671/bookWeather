package com.bw.bookweather.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.bw.bookweather.db.City;
import com.bw.bookweather.db.County;
import com.bw.bookweather.db.Province;
import com.bw.bookweather.json.WeatherJson;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zxc on 2020/1/11.
 */

public class Utility {
    public static boolean handlePronvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject provinceJSON = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setName(provinceJSON.getString("name"));
                    province.setCode(provinceJSON.getString("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response, String parentId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject cityJSON = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setName(cityJSON.getString("name"));
                    city.setCode(cityJSON.getString("id"));
                    city.setParentId(parentId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(String response, String parentId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setName(jsonObject.getString("name"));
                    county.setCode(jsonObject.getString("weather_id").substring(2));
                    county.setParentId(parentId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static WeatherJson handleWeatherJsonResponse(String response) {
        Gson gson = new Gson();
        WeatherJson weatherJson = gson.fromJson(response, WeatherJson.class);
//        Log.d(TAG, "handleWeatherJsonResponse: " + weatherJson.status);
        return weatherJson;

    }

    private static final String TAG = "Utility---------";


    public static boolean versionUpdate(NotificationManager nm) {
//        26通知
//      .setChannelId("channel_26")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "warnNotify: 26+");
            String id = "channel_26";
            String description = "channel_26+";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel nc = new NotificationChannel(id, "channel_26", importance);
            //  mChannel.setDescription(description);
            //  mChannel.enableLights(true);
            //  mChannel.setLightColor(Color.RED);
            //  mChannel.enableVibration(true);
            //  mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            nm.createNotificationChannel(nc);
        }
        return true;
    }
}
