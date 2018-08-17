package com.crypt.adclock.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crypt.adclock.util.converters.RepeatDaysConverter;
import com.crypt.adclock.util.converters.TimeConverter;
import com.crypt.adclock.util.converters.UriConverter;

import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Ghito on 08-Mar-18.
 */

@Entity(tableName = "alarms")
@TypeConverters({TimeConverter.class, RepeatDaysConverter.class, UriConverter.class})
public class Alarm {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "entryid")
    private String id;

    @Nullable
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "time")
    private Time time;

    @NonNull
    @ColumnInfo(name = "repeat_days")
    private ArrayList<Boolean> repeatDays;

    @ColumnInfo(name = "is_active")
    private boolean isActive;

    @NonNull
    @ColumnInfo(name = "ringtoneUri")
    private Uri ringtoneUri;


    public Alarm(@NonNull String id, @Nullable String title, @NonNull Time time,
                 @NonNull ArrayList<Boolean> repeatDays, boolean isActive,
                 @NonNull Uri ringtoneUri) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.repeatDays = repeatDays;
        this.isActive = isActive;
        this.ringtoneUri = ringtoneUri;
    }

    /**
     * Use this constructor to create a new active Alarm.
     */
    @Ignore
    public Alarm(@Nullable String title, @NonNull Time time,
                 @NonNull ArrayList<Boolean> repeatDays, boolean isActive,
                 @NonNull Uri ringtoneUri) {
        this(UUID.randomUUID().toString(), title, time, repeatDays,
                isActive, ringtoneUri);
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
    public Time getTime() {
        return time;
    }

    @NonNull
    public ArrayList<Boolean> getRepeatDays() {
        return repeatDays;
    }

    public boolean isActive() {
        return isActive;
    }

    @NonNull
    public Uri getRingtoneUri() {
        return ringtoneUri;
    }

    public void setTitle(@Nullable String title) {
        this.title = title;
    }

    public void setTime(@NonNull Time time) {
        this.time = time;
    }

    public void setRepeatDays(@NonNull ArrayList<Boolean> repeatDays) {
        this.repeatDays = repeatDays;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRingtoneUri(@NonNull Uri ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    // Returns true if alarm should ring on this day of week.
    // Day is passed in zero-based format
    public boolean isRepeatingOn(int day) {
        return repeatDays.get(day);
    }

}
