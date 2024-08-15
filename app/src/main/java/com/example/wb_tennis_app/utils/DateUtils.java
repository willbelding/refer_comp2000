package com.example.wb_tennis_app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtils {

    public static int getDayOfWeek(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(dateString));
            return calendar.get(Calendar.DAY_OF_WEEK) - 1;  // Calendar.DAY_OF_WEEK returns 1 for Sunday, we adjust to start from 0
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Invalid date
        }
    }
}