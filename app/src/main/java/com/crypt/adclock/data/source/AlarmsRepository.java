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
    private Alarm mCachedRetainedAlarm;
    private String mRecentlySavedAlarmId;

    private boolean mCacheIsValid = false;

    @Inject
    AlarmsRepository(@Local AlarmsDataSource alarmsDataSource) {
        mLocalDataSource = alarmsDataSource;
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
    public void retain(@NonNull Alarm alarm) {
        mCachedRetainedAlarm = alarm;
        mLocalDataSource.retain(alarm);
    }

    @Override
    public void restoreRetained(@NonNull LoadAlarmCallback callback) {
        if (mCachedRetainedAlarm != null) {
            callback.onLoaded(mCachedRetainedAlarm);
            mCachedRetainedAlarm = null;
            return;
        }

        mLocalDataSource.restoreRetained(callback);
    }

    @Override
    public void getAll(@NonNull final LoadAllAlarmsCallback callback) {
        if (mCacheIsValid) {
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
    public void getRecentlySaved(@NonNull LoadAlarmCallback callback) {
        if (mCacheIsValid && mRecentlySavedAlarmId != null) {
            Alarm recentlySavedAlarm = mCachedAlarms.get(mRecentlySavedAlarmId);
            if (recentlySavedAlarm != null) {
                callback.onLoaded(recentlySavedAlarm);
            }
            return;
        }
        callback.onNotAvailable();
    }

    @Override
    public void save(@NonNull Alarm alarm) {
        mRecentlySavedAlarmId = alarm.getId();
        mCachedAlarms.put(alarm.getId(), alarm);
        mLocalDataSource.save(alarm);
    }

    @Override
    public void update(@NonNull Alarm alarm) {
        mRecentlySavedAlarmId = alarm.getId();
        mCachedAlarms.put(alarm.getId(), alarm);
        mLocalDataSource.update(alarm);
    }

    @Override
    public void deleteAll() {
        mCachedAlarms.clear();
        mLocalDataSource.deleteAll();
    }

    @Override
    public void deleteAlarms(List<Alarm> alarms) {
        for (Alarm alarm : alarms) {
            mCachedAlarms.remove(alarm.getId());
        }
        mLocalDataSource.deleteAlarms(alarms);
    }
}
