package com.crypt.adclock.util.converters;

import android.arch.persistence.room.TypeConverter;

import org.jetbrains.annotations.Contract;
import org.joda.time.DateTime;

public final class TimeConverter {

    @Contract("null -> null")
    @TypeConverter
    public static DateTime toDate(String timeString) {
        if (timeString == null) {
            return null;
        }

        return DateTime.parse(timeString);
    }

    @Contract("null -> null")
    @TypeConverter
    public static String fromDate(DateTime time) {
        if (time == null) {
            return null;
        }

        return time.toString();
    }
}
