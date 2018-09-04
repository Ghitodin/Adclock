package com.crypt.adclock.data.source;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;

import com.crypt.adclock.data.source.local.AlarmsDao;
import com.crypt.adclock.data.source.local.AlarmsDatabase;
import com.crypt.adclock.data.source.local.AlarmsLocalDataSource;
import com.crypt.adclock.util.AppExecutors;
import com.crypt.adclock.util.DiskIOThreadExecutor;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class AlarmsRepositoryModule {
    private static final int THREAD_COUNT = 3;

    @Singleton
    @Binds
    @Local
    abstract AlarmsDataSource provideLocalDataSource(AlarmsLocalDataSource dataSource);

    @NonNull
    @Singleton
    @Provides
    static AlarmsDatabase provideDb(Application context) {
        return Room.databaseBuilder(context, AlarmsDatabase.class, "Alarms.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    static AlarmsDao provideDao(AlarmsDatabase database) {
        return database.alarmsDao();
    }

    @Singleton
    @Provides
    static AppExecutors provideAppExecutors() {
        return new AppExecutors(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());
    }
}
