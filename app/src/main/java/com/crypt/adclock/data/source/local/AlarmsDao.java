package com.crypt.adclock.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.crypt.adclock.data.Alarm;

import java.util.List;

/**
 * Created by Ghito on 08-Mar-18.
 */

@Dao
public interface AlarmsDao {
    @Query("SELECT * FROM Alarms")
    List<Alarm> getAll();

    @Query("SELECT * FROM Alarms WHERE entryid = :alarmId")
    Alarm get(String alarmId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Alarm alarm);

    @Update
    int update(Alarm alarm);

    @Query("DELETE FROM Alarms")
    void deleteAll();

    @Query("DELETE FROM Alarms WHERE entryid = :alarmId")
    void delete(String alarmId);
}
