package com.bw.bookweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bw.bookweather.db.AppConfig;
import com.bw.bookweather.json.ForecastJson;
import com.bw.bookweather.json.WeatherJson;
import com.bw.bookweather.util.ConfigEnum;
import com.bw.bookweather.util.ConfigUtil;
import com.bw.bookweather.util.HttpUtil;
import com.bw.bookweather.util.Utility;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "WeatherActivity-------";
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView pm10Text;
    private TextView humidityText;
    private TextView noticeText;
    private TextView noteText;
    public SwipeRefreshLayout swipeRefresh;
    public DrawerLayout drawerLayout;
    public Button navButton;
    public Button configBt;
    public Button refreshImgBt;
    public ImageView bingImgView;

    private static Date updateDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        manager.cancel(1);
        //todo

//初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weatherLayout);
        titleCity = (TextView) findViewById(R.id.titleCity);
        titleUpdateTime = (TextView) findViewById(R.id.titleUpdateTime);
        degreeText = (TextView) findViewById(R.id.degreeText);
        forecastLayout = (LinearLayout) findViewById(R.id.forecastLayout);
        aqiText = (TextView) findViewById(R.id.aqiText);
        pm25Text = (TextView) findViewById(R.id.pm25Text);
        noticeText = (TextView) findViewById(R.id.noticeText);
        noteText = (TextView) findViewById(R.id.noteText);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        drawerLayout = findViewById(R.id.drawerLayout);
        navButton = findViewById(R.id.navButton);
        configBt = findViewById(R.id.configBt);
        refreshImgBt = findViewById(R.id.refreshImgBt);
        bingImgView = findViewById(R.id.bingImgView);
        pm10Text = findViewById(R.id.pm10Text);;
        humidityText = findViewById(R.id.humidityText);

        navButton.setOnClickListener(this);
        configBt.setOnClickListener(this);
        refreshImgBt.setOnClickListener(this);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String weatherStr = sp.getString("weather", null);
        final String countyCode = getIntent().getStringExtra("countyCode");
        if (!TextUtils.isEmpty(weatherStr)) {
            //有缓存时
            WeatherJson weather = Utility.handleWeatherJsonResponse(weatherStr);
            if (weather==null){
                return;
            }
            showWeatherInfo(weather);
        } else {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(countyCode);
        }
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(countyCode);
            }
        });
        loadBingImg();
    }

    public void requestWeather(String countyCode2) {
//        String countyCode = "101010300";
        MainActivity ma = new MainActivity();
        String countyCode1 = getIntent().getStringExtra("countyCode");

        AppConfig appConfig = ConfigUtil.getAppConfigMap().get(ConfigEnum.LOCATION.getValue());
        String countyCode = appConfig.getValue();
        String url = "http://t.weather.sojson.com/api/weather/city/" + countyCode;

        Log.d(TAG, "requestWeather: " + url);
        HttpUtil.sendRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();

                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String weatherStr = response.body().string();
                Log.d(TAG, "onResponse: hwj");
                final WeatherJson weatherJson = Utility.handleWeatherJsonResponse(weatherStr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weatherJson != null && "200".equals(weatherJson.status)) {
                            SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                            edit.putString("weather", weatherStr);
                            edit.apply();
                            showWeatherInfo(weatherJson);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
//        loadBingImg();
    }

    public void loadBingImg() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String bingImg = sp.getString("bingImg", null);
        loadBingImg(bingImg);
    }

    public void loadBingImg(String bingImg) {
        if (TextUtils.isEmpty(bingImg)) {
            String requestBingImg = "http://guolin.tech/api/bing_pic";
            HttpUtil.sendRequest(requestBingImg, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    String responseStr = response.body().string();
                    SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                    edit.putString("bingImg", responseStr);
                    edit.apply();
                }
            });
        }
        if (TextUtils.isEmpty(bingImg)) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            bingImg=sp.getString("bingImg",null);
            if (TextUtils.isEmpty(bingImg)){
                Toast.makeText(this, "获取图片失败。", Toast.LENGTH_SHORT).show();
            }else {
                Glide.with(this).load(bingImg).into(bingImgView);
            }
        }else {
            Glide.with(this).load(bingImg).into(bingImgView);
        }
    }

    private void showWeatherInfo(WeatherJson weather) {
        if (weather != null && "200".equals(weather.status)) {
            String cityName = weather.cityJson.cityName;
            String updateTime = weather.cityJson.updateTime;
            String degree = weather.weatherDataJson.wendu + "℃";
            String notice = weather.weatherDataJson.forecastJsonList.get(0).notice;
            titleCity.setText(cityName);
            titleUpdateTime.setText(updateTime+"发布");
            degreeText.setText(degree);
//            weatherInfoText.setText(notice);
            forecastLayout.removeAllViews();
            List<ForecastJson> forecastJsonList = weather.weatherDataJson.forecastJsonList;
            for (int i = 0; i < 7; i++) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
                ForecastJson forecast = forecastJsonList.get(i);
                TextView dateText = (TextView) view.findViewById(R.id.dateText);
                TextView infoText = (TextView) view.findViewById(R.id.infoText);
                TextView windSpeedText = (TextView) view.findViewById(R.id.windSpeedText);
                TextView maxText = (TextView) view.findViewById(R.id.maxText);
                TextView minText = (TextView) view.findViewById(R.id.minText);;

                String ganmao = weather.weatherDataJson.ganmao;
                if (!TextUtils.isEmpty(ganmao))
                    noticeText.setText(ganmao);
                dateText.setText(forecast.ymd.substring(5) + "/" + forecast.week);
                infoText.setText(forecast.type);
                windSpeedText.setText(forecast.fx+ "/" + forecast.fl);
                maxText.setText(forecast.high);
                minText.setText(forecast.low);
                forecastLayout.addView(view);
            }

            pm10Text.setText(weather.weatherDataJson.pm10);
            humidityText.setText(weather.weatherDataJson.shidu);
                aqiText.setText(forecastJsonList.get(0).aqi + "/" + weather.weatherDataJson.quality);
                pm25Text.setText(weather.weatherDataJson.pm25);
            noteText.setText(forecastJsonList.get(0).notice);
//            String comfort = "舒适度：" + weather.suggestion.comfort.info;
//            String carWash = "洗车指数：" + weather.suggestion.carWash.info;
//            String sport = "运动建议：" + weather.suggestion.sport.info;
//            comfortText.setText(comfort);
//            sportText.setText(sport);
//            carWashText.setText(carWash);
            weatherLayout.setVisibility(View.VISIBLE);

//            new ChooseAreaFragment().queryConfigs();
        } else {
            Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navButton:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.refreshImgBt:
                loadBingImg("");
                break;
            case R.id.configBt:
                Intent intent = new Intent(this,ConfigActivity.class);
                intent.putExtra("activity", "configActivity");
                startActivityForResult(intent,3);
                break;
//            case R.id.titleCity:
//                Toast.makeText(this, "...", Toast.LENGTH_SHORT).show();
//                break;
        }
    }
}