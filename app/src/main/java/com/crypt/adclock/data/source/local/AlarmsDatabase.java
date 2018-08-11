package com.crypt.adclock.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.crypt.adclock.data.Alarm;

/**
 * Created by Ghito on 08-Mar-18.
 */
@Database(entities = {Alarm.class}, version = 1)
public abstract class AlarmsDatabase extends RoomDatabase {

    public abstract AlarmsDao alarmsDao();
}
