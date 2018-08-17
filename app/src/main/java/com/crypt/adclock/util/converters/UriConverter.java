package com.crypt.adclock.util.converters;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

import org.jetbrains.annotations.Contract;

public final class UriConverter {

    @Contract("null -> null")
    @TypeConverter
    public static Uri toUri(String string) {
        return string == null ? null : Uri.parse(string);
    }

    @Contract("null -> null")
    @TypeConverter
    public static String fromUri(Uri uri) {
        return uri == null ? null : uri.toString();
    }
}
