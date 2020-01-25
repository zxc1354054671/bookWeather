package com.bw.bookweather;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bw.bookweather.service.WarnService;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity------";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int warnNotifyId1 = getIntent().getIntExtra("warnNotifyId1", 1);
        int warnNotifyId2 = getIntent().getIntExtra("warnNotifyId2", 2);
        manager.cancel(warnNotifyId1);
        manager.cancel(warnNotifyId2);
        manager.cancelAll();
        Log.d(TAG, "onCreate: cancel");

        Log.d(TAG, "onCreate: 0");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getString("weather", null) != null) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

//        Intent intentAutoUpdateService = new Intent(this, AutoUpdateService.class);
                Intent intentWarnService = new Intent(getApplicationContext(), WarnService.class);
//
//        startService(intentAutoUpdateService);
                startService(intentWarnService);

            }
        }).start();
    }

}
