package com.crypt.adclock.data.source;

import android.support.annotation.NonNull;

import com.crypt.adclock.data.Alarm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Ghito on 08-Mar-18.
 */

@Singleton
public class AlarmsRepository implements AlarmsDataSource {
    private final AlarmsDataSource mLocalDataSource;

    private Map<String, Alarm> mCachedAlarms = new LinkedHashMap<>();

    private boolean mCacheIsValid = false;

    @Inject
    AlarmsRepository(@Local AlarmsDataSource tasksLocalDataSource) {
        mLocalDataSource = tasksLocalDataSource;
    }

    private void refreshCache(@NonNull List<Alarm> alarms) {
        mCacheIsValid = false;

        mCachedAlarms.clear();
        for (Alarm alarm : alarms) {
            mCachedAlarms.put(alarm.getId(), alarm);
        }

        mCacheIsValid = true;
    }

    @Override
    public void getAll(@NonNull final LoadAllAlarmsCallback callback) {
        if (!mCacheIsValid) {
            callback.onLoaded(new ArrayList<>(mCachedAlarms.values()));
            return;
        }
        mLocalDataSource.getAll(new LoadAllAlarmsCallback() {
            @Override
            public void onLoaded(List<Alarm> alarms) {
                refreshCache(alarms);
                callback.onLoaded(new ArrayList<>(mCachedAlarms.values()));
            }

            @Override
            public void onNotAvailable() {
                callback.onNotAvailable();
            }
        });
    }

    @Override
    public void get(@NonNull String id, @NonNull final LoadAlarmCallback callback) {
        if (mCacheIsValid) {
            Alarm alarm = mCachedAlarms.get(id);
            if (alarm != null) {
                callback.onLoaded(alarm);
                return;
            }
        }

        mLocalDataSource.get(id, new LoadAlarmCallback() {
            @Override
            public void onLoaded(Alarm alarm) {
                mCachedAlarms.put(alarm.getId(), alarm);
                callback.onLoaded(alarm);
            }

            @Override
            public void onNotAvailable() {
                callback.onNotAvailable();
            }
        });
    }

    @Override
    public void save(@NonNull Alarm alarm) {

    }

    @Override
    public void update(@NonNull Alarm alarm) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void delete(@NonNull String alarmId) {

    }
}
