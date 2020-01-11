package com.bw.bookweather.util;

import android.text.TextUtils;

import com.bw.bookweather.db.City;
import com.bw.bookweather.db.County;
import com.bw.bookweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;

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
    public static boolean handleCityResponse(String response,String parentId) {
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
    public static boolean handleCountyResponse(String response,String parentId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
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
}
