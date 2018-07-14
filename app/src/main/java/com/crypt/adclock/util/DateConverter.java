package com.crypt.adclock.util;

import android.arch.persistence.room.TypeConverter;

import org.jetbrains.annotations.Contract;

import java.util.Date;

public class DateConverter {

    @Contract("null -> null; !null -> !null")
    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @Contract("null -> null")
    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
