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

    @Inject
    AlarmsLocalDataSource(@NonNull AppExecutors executors, @NonNull AlarmsDao dao) {
        mExecutors = executors;
        mDao = dao;
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
    public void delete(@NonNull final String alarmId) {
        Runnable diskAccessRunnable = new Runnable() {
            @Override
            public void run() {
                mDao.delete(alarmId);
            }
        };
        mExecutors.diskIO().execute(diskAccessRunnable);
    }
}
