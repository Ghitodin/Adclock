package com.crypt.adclock.data.source.local;

import android.support.annotation.NonNull;

import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.source.AlarmsDataSource;
import com.crypt.adclock.util.AppExecutors;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Ghito on 08-Mar-18.
 */

@Singleton
public class AlarmsLocalDataSource implements AlarmsDataSource {

    private final AlarmsDao mDao;
    private final AppExecutors mExecutors;
    private Alarm mRetainedAlarm;

    @Inject
    public AlarmsLocalDataSource(@NonNull AppExecutors executors, @NonNull AlarmsDao dao) {
        mExecutors = executors;
        mDao = dao;
    }

    @Override
    public void retain(@NonNull Alarm alarm) {
        mRetainedAlarm = alarm;
    }

    @Override
    public void restoreRetained(@NonNull LoadAlarmCallback callback) {
        if (mRetainedAlarm != null) {
            callback.onLoaded(mRetainedAlarm);
            mRetainedAlarm = null;
        } else {
            callback.onNotAvailable();
        }
    }

    @Override
    public void getAll(@NonNull final LoadAllAlarmsCallback callback) {
        Runnable diskAccessRunnable = new Runnable() {
            @Override
            public void run() {
                final List<Alarm> alarms = mDao.getAll();
                mExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!alarms.isEmpty()) {
                            callback.onLoaded(alarms);
                        } else {
                            callback.onNotAvailable();
                        }
                    }
                });
            }
        };
        mExecutors.diskIO().execute(diskAccessRunnable);
    }

    @Override
    public void get(@NonNull final String id, @NonNull final LoadAlarmCallback callback) {
        Runnable diskAccessRunnable = new Runnable() {
            @Override
            public void run() {
                final Alarm alarm = mDao.get(id);
                mExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (alarm != null) {
                            callback.onLoaded(alarm);
                        } else {
                            callback.onNotAvailable();
                        }
                    }
                });
            }
        };
        mExecutors.diskIO().execute(diskAccessRunnable);
    }

    @Override
    public void getRecentlySaved(@NonNull LoadAlarmCallback callback) {
        // Recently saved alarms cached only in the Repository cache
    }

    @Override
    public void save(@NonNull final Alarm alarm) {
        Runnable diskAccessRunnable = new Runnable() {
            @Override
            public void run() {
                mDao.insert(alarm);
            }
        };
        mExecutors.diskIO().execute(diskAccessRunnable);
    }

    @Override
    public void update(@NonNull final Alarm alarm) {
        Runnable diskAccessRunnable = new Runnable() {
            @Override
            public void run() {
                mDao.update(alarm);
            }
        };
        mExecutors.diskIO().execute(diskAccessRunnable);
    }

    @Override
    public void deleteAll() {
        Runnable diskAccessRunnable = new Runnable() {
            @Override
            public void run() {
                mDao.deleteAll();
            }
        };
        mExecutors.diskIO().execute(diskAccessRunnable);
    }

    @Override
    public void deleteAlarms(final List<Alarm> alarms) {
        Runnable diskAccessRunnable = new Runnable() {
            @Override
            public void run() {
                mDao.deleteAlarms(alarms);
            }
        };
        mExecutors.diskIO().execute(diskAccessRunnable);
    }
}
