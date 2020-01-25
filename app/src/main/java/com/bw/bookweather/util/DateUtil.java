package com.bw.bookweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zxc on 2020/1/22.
 */

public class DateUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    public static String dateToString(Date date) {
        String formatDate = sdfDate.format(date);
        return formatDate;
    }

    public static String dateTimeToString(Date date) {
        String formatDate = sdf.format(date);
        return formatDate;
    }

    public static String timeMillisToString(long time) {
        String formatDate = sdf.format(new Date(time));
        return formatDate;
    }

}
