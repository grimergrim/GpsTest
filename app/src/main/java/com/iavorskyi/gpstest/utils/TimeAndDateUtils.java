package com.iavorskyi.gpstest.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeAndDateUtils {

    String getDateAsStringFromSystemTime(long timeInMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String colverMillisecondsToAmtTimeFormat(long timeInMillis) {
        return "/Date(" + timeInMillis + ")/";
    }

    String getCurrentTimeForFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return simpleDateFormat.format(calendar.getTime());
    }

}
