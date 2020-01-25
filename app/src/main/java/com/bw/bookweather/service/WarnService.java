package com.bw.bookweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bw.bookweather.MainActivity;
import com.bw.bookweather.R;
import com.bw.bookweather.json.ForecastJson;
import com.bw.bookweather.json.WeatherDataJson;
import com.bw.bookweather.json.WeatherJson;
import com.bw.bookweather.util.DateUtil;
import com.bw.bookweather.util.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;

//import android.icu.util.Calendar;
//import android.icu.util.TimeZone;


/**
 * Created by zxc on 2020/1/21.
 */

public class WarnService extends BaseService {
    //    提醒时间
    int hour = 9;
    int minute = 0;

    //    提醒条件
    Integer windLevel = 6;
    Double min = -10d;
    Double max = 30d;
    String[] warnTypeArr = {"雨", "雪", "雾", "雹", "霾"};
    List<String> warnList = new ArrayList<>();

    private static final String TAG = "WarnService-----------";


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ---");

        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();

        //得到日历实例，主要是为了下面的获取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        //是设置日历的时间，主要是让日历的年月日和当前同步
        calendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒  设置的为7点
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        //设置在几分提醒  设置的为0分
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

//        加两小时
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(calendar.getTime());
        calendar1.set(Calendar.HOUR_OF_DAY, hour + 2);

        //时间范围
        long time = calendar.getTimeInMillis();
        long time1 = calendar1.getTimeInMillis();

        if (systemTime >= time) {
            if (systemTime <= time1) {
//                warnTest();
                updateWeather();
                warnNotify();
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent1 = new Intent(this, WarnService.class);
        Intent intent1 = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent1, 0);
        am.cancel(pi);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 1000 * 30, (1000 * 60 * 60 * 24), pi);
//        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis() + 1000 * 3, (1000 * 3), pi);
//        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.currentThreadTimeMillis() + 1000 * 3, (1000 * 60 * 60 * 24), pi);
//        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime,(1000 * 60 * 60 * 24), pi);
//        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }


    public void warnNotify() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String weatherStr = sp.getString("weather", null);
        WeatherJson weatherJson = Utility.handleWeatherJsonResponse(weatherStr);
        if (weatherJson == null) {
            return;
        }

        WeatherDataJson wdj = weatherJson.weatherDataJson;
        List<ForecastJson> forecastJsonList = wdj.forecastJsonList;
        ForecastJson fj = forecastJsonList.get(0);
        ForecastJson fj1 = forecastJsonList.get(1);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        校验日期
        if (DateUtil.dateToString(new Date(System.currentTimeMillis())).equals(fj.ymd)) {

            Utility.versionUpdate(nm);

            TreeMap<Double, String> aqiMap = new TreeMap<>();
            aqiMap.put(50D, "优");
            aqiMap.put(100D, "良");
            aqiMap.put(150D, "轻度污染");
            aqiMap.put(200D, "中度污染");
            aqiMap.put(300D, "重度污染");
            aqiMap.put(1000D, "严重污染");

            if (warnCheck(fj1)) {
//                明日
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("warnNotifyId2", 2);
                PendingIntent pi2 = PendingIntent.getActivity(this, 0, intent, 0);
                int fl = Integer.parseInt(fj1.fl.substring(fj1.fl.length() - 2, fj1.fl.length() - 1));
                double aqiD = Double.parseDouble(fj1.aqi);
                String aqiStr="";
                for (Double d : aqiMap.keySet()) {
                    if (aqiD<=d){
                        aqiStr=aqiMap.get(d);
                        break;
                    }
                }

                String notifyStr = "";
                notifyStr += fj1.type;
                notifyStr += "  " + fj1.low;
                notifyStr += "  " + fj1.high;
                notifyStr += "  " + "空气质量：" + aqiStr;
                if (fl >= windLevel)
                    notifyStr += "  " + fj1.fx + "/" + fj1.fl;
                Notification notification = new NotificationCompat.Builder(this, Notification.CATEGORY_ALARM)
                        .setContentTitle("天气预报-明日")
                        .setContentText(notifyStr)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.logo_weather_dark)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_weather))
                        .setContentIntent(pi2)
                        .setAutoCancel(true)
                        .setChannelId("channel_26")
                        .build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                startForeground(2, notification);
                nm.notify(2, notification);
                Log.d(TAG, "warnNotify: 2");
            }

            if (warnCheck(fj)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("warnNotifyId1", 1);
                PendingIntent pi2 = PendingIntent.getActivity(this, 0, intent, 0);
                int fl = Integer.parseInt(fj.fl.substring(fj.fl.length() - 2, fj.fl.length() - 1));
                String notifyStr = "";
                notifyStr += fj.type;
                notifyStr += "  " + fj.low;
                notifyStr += "  " + fj.high;
                notifyStr += "  " + "天气质量：" + wdj.quality;
                if (fl >= windLevel)
                    notifyStr += "  " + fj.fx + "/" + fj.fl;
                Notification notification = new NotificationCompat.Builder(this, "default")
                        .setContentTitle("天气预报-今日")
                        .setContentText(notifyStr)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.logo_weather_dark)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_weather))
                        .setContentIntent(pi2)
                        .setAutoCancel(true)
                        .setChannelId("channel_26")
                        .build();
                notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                startForeground(1, notification);

                nm.notify(1, notification);
                Log.d(TAG, "warnNotify: 1");
            }

        }
    }

    public boolean warnCheck(ForecastJson fj) {
        Double low = Double.parseDouble(fj.low.substring(2, fj.low.length() - 1));
        Double high = Double.parseDouble(fj.high.substring(2, fj.high.length() - 1));
        String type = fj.type;
        int fl = Integer.parseInt(fj.fl.substring(fj.fl.length() - 2, fj.fl.length() - 1));
        Collections.addAll(warnList, warnTypeArr);
//        空气质量
        if (Double.parseDouble(fj.aqi) > 100)
            return true;

//        天气情况
        for (String s : warnList) {
            if (type.contains(s)) {
                return true;
            }
        }

//        温度风级
        if (low <= min || high >= max || fl >= windLevel)
            return true;

        return false;
    }

    public void warnTest() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Utility.versionUpdate(nm);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("warnNotifyId1", 1);
        PendingIntent pi2 = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, "default")
                .setContentTitle("天气预报-test")
                .setContentText("test")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.logo_weather_dark)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo_weather))
                .setContentIntent(pi2)
                .setAutoCancel(true)
                .setChannelId("channel_26")
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                notification.flags |= Notification.FLAG_AUTO_CANCEL;
//                startForeground(1, notification);

        nm.notify(1, notification);
    }

}
