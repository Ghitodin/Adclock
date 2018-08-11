package com.crypt.adclock.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crypt.adclock.util.DateConverter;

import java.sql.Time;
import java.util.UUID;

/**
 * Created by Ghito on 08-Mar-18.
 */

@Entity(tableName = "alarms")
@TypeConverters(DateConverter.class)
public class Alarm {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private final String id;

    @Nullable
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "date")
    private final Time date;

    @NonNull
    @ColumnInfo(name = "repeat_type")
    private final int repeatType;

    @ColumnInfo(name = "custom_repeat_days")
    private int customRepeatDays;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @NonNull
    @ColumnInfo(name = "ringtone")
    private String ringtone;


    public Alarm(@Nullable String id, @Nullable String title, @NonNull Time date,
                 @NonNull int repeatType, int customRepeatDays,
                 boolean isActive, String ringtone) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.repeatType = repeatType;
        this.customRepeatDays = customRepeatDays;
        this.isActive = isActive;
        this.ringtone = ringtone;
    }

    /**
     * Use this constructor to create a new active Alarm.
     */
    @Ignore
    public Alarm(@Nullable String title, @NonNull Time date,
                 @NonNull int repeatType, int customRepeatDays,
                 boolean isActive, String ringtone) {
        this(UUID.randomUUID().toString(), title, date, repeatType, customRepeatDays,
                isActive, ringtone);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getTitle() {
        return title;
    }

    @NonNull
    public Time getDate() {
        return date;
    }

    @NonNull
    public int getRepeatType() {
        return repeatType;
    }

    public int getCustomRepeatDays() {
        return customRepeatDays;
    }

    public boolean isActive() {
        return isActive;
    }

    @NonNull
    public String getRingtone() {
        return ringtone;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }
    /*
    public void setDate(@NonNull Time date) {
        this.date = date;
    }
    */
    /*
    public void setRepeatType(@NonNull int repeatType) {
        this.repeatType = repeatType;
    }
    */

    public void setCustomRepeatDays(int customRepeatDays) {
        this.customRepeatDays = customRepeatDays;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRingtone(@NonNull String ringtone) {
        this.ringtone = ringtone;
    }

}
