package com.crypt.adclock.util;

import android.arch.persistence.room.TypeConverter;

import org.jetbrains.annotations.Contract;

import java.sql.Time;

public class DateConverter {

    @Contract("null -> null; !null -> !null")
    @TypeConverter
    public static Time toDate(Long timeLong){
        return timeLong == null ? null: new Time(timeLong);
    }

    @Contract("null -> null")
    @TypeConverter
    public static Long fromDate(Time time){
        return time == null ? null : time.getTime();
    }
}
