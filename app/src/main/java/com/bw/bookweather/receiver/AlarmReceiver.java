package com.bw.bookweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver------";
    @Override
    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        if (action == MainActivity.INTENT_ALARM_LOG) {
//            Log.d("AlarmReceiver", "log log log");
//        }
        Log.d(TAG, "onReceive: warn        ok");
        new AlertDialog.Builder(context)
                .setTitle("标题")
                .setMessage("简单的消息提示框")
                .setPositiveButton("确定", null)
                .show();
        Toast.makeText(context, "...", Toast.LENGTH_LONG).show();
        //当系统到我们设定的时间点的时候会发送广播，执行这里
    }
}
