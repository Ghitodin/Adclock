package com.crypt.adclock.util.converters;

import android.arch.persistence.room.TypeConverter;

import org.jetbrains.annotations.Contract;

import java.sql.Time;

public final class TimeConverter {

    @Contract("null -> null")
    @TypeConverter
    public static Time toDate(String timeString) {
        if (timeString == null) {
            return null;
        }

        return Time.valueOf(timeString);
    }

    @Contract("null -> null")
    @TypeConverter
    public static String fromDate(Time time) {
        if (time == null) {
            return null;
        }

        return time.toString();
    }
}
