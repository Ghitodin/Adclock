package com.crypt.adclock.util;

import android.arch.persistence.room.TypeConverter;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

public final class RepeatDaysConverter {
    private static int DAYS_IN_WEEK = 7;
    private static String SEPARATOR = ",";

    @Contract("null -> null")
    @TypeConverter
    public static ArrayList<Boolean> toRepeatDays(String repeatDaysString) {
        if (repeatDaysString == null) {
            return null;
        }

        String[] repeatDaysStringArray = repeatDaysString.split(SEPARATOR);
        if (repeatDaysStringArray.length != DAYS_IN_WEEK) {
            return null;
        }

        ArrayList<Boolean> result = new ArrayList<>();
        for (String repeatDay : repeatDaysStringArray) {
            result.add(Boolean.parseBoolean(repeatDay));
        }
        return result;
    }

    @Contract("null -> null")
    @TypeConverter
    public static String fromRepeatDays(ArrayList<Boolean> repeatDays) {
        if (repeatDays == null || repeatDays.size() != DAYS_IN_WEEK) {
            return null;
        }

        StringBuilder repeatDaysStringBuilder = new StringBuilder();
        for (Boolean day : repeatDays) {
            repeatDaysStringBuilder.append(day.toString());
            repeatDaysStringBuilder.append(SEPARATOR);
        }
        // Remove last separator (true, true, true,)
        repeatDaysStringBuilder.deleteCharAt(repeatDaysStringBuilder.length() - 1);
        return repeatDaysStringBuilder.toString();
    }
}
