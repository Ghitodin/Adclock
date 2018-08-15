package com.crypt.adclock.util;

import android.util.Log;
import java.text.DateFormatSymbols;

public final class WeekDays {

    private static final String TAG = "DaysOfWeek";
    // DAY_OF_WEEK constants in Calendar class are not zero-based
    // Alarm class saves repeats to boolean list, with indexes starting from zero
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    private static final String[] LABELS = new DateFormatSymbols().getShortWeekdays();

    private static WeekDays sInstance;

    public static WeekDays getInstance() {
        // For the time being we always use sunday as the prefered first day, so user can`t define
        // his own first day.

        if (sInstance == null) {
            sInstance = new WeekDays();
        }
        Log.d(TAG, sInstance.toString());
        return sInstance;
    }

    /**
     * @param weekday the zero-based index of the week day you would like to get the label for.
     */
    public static String getLabel(int weekday) {
        // This array is returned from DateFormatSymbols.getShortWeekdays().
        return LABELS[weekday + 1];
    }

    private WeekDays() {

    }

}

