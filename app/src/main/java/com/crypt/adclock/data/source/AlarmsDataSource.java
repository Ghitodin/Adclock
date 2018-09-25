package com.crypt.adclock.data.source;

import android.support.annotation.NonNull;

import com.crypt.adclock.data.Alarm;

import java.util.List;

/**
 * Created by Ghito on 08-Mar-18.
 */

public interface AlarmsDataSource {

    interface LoadAllAlarmsCallback {

        void onLoaded(List<Alarm> alarms);

        void onNotAvailable();
    }

    interface LoadAlarmCallback {

        void onLoaded(Alarm alarm);

        void onNotAvailable();

    }

    void retain(@NonNull Alarm alarm); // Retain alarm from activity destroying

    void restoreRetained(@NonNull LoadAlarmCallback callback);

    void getAll(@NonNull LoadAllAlarmsCallback callback);

    void get(@NonNull String id, @NonNull LoadAlarmCallback callback);

    void getRecentlySaved(@NonNull LoadAlarmCallback callback);

    void save(@NonNull Alarm alarm);

    void update(@NonNull Alarm alarm);

    void deleteAll();

    void deleteAlarms(List<Alarm> alarms);
}
