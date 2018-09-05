package com.crypt.adclock.alarms;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.source.AlarmsDataSource;
import com.crypt.adclock.data.source.AlarmsRepository;
import com.crypt.adclock.di.ActivityScoped;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Ghito on 08-Mar-18.
 */
@ActivityScoped
public class AlarmsPresenter implements AlarmsContract.Presenter,
        AlarmsDataSource.LoadAllAlarmsCallback {
    @Nullable
    private AlarmsContract.View mAlarmsView;
    private final AlarmsDataSource mRepository;

    @Inject
    AlarmsPresenter(@NonNull AlarmsRepository repository) {
        mRepository = repository;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // Show snackbar
        if (Activity.RESULT_OK == resultCode) {
            mRepository.getRecentlySaved(new AlarmsDataSource.LoadAlarmCallback() {
                @Override
                public void onLoaded(Alarm alarm) {
                    if (mAlarmsView != null) {
                        mAlarmsView.showAddedAlarmMessage(alarm);
                    }
                }

                @Override
                public void onNotAvailable() {
                    if (mAlarmsView != null) {
                        mAlarmsView.showErrorMessage();
                    }
                }
            });
        }
    }

    @Override
    public void loadAlarms() {
        mRepository.getAll(this);
    }

    @Override
    public void addNewAlarm() {
        if (mAlarmsView != null) {
            mAlarmsView.showAddAlarm();
        }
    }

    @Override
    public void activateAlarm(@NonNull Alarm activeAlarm) {

    }

    @Override
    public void editAlarm(Alarm alarm) {
        if (mAlarmsView != null) {
            mAlarmsView.showEditAlarm(alarm);
        }
    }

    @Override
    public void takeView(AlarmsContract.View view) {
        mAlarmsView = view;
        loadAlarms();
    }

    @Override
    public void dropView() {
        mAlarmsView = null;
    }

    @Override
    public void onLoaded(List<Alarm> alarms) {
        if (mAlarmsView != null) {
            mAlarmsView.showAlarms(alarms);
        }
    }

    @Override
    public void onNotAvailable() {
        if (mAlarmsView != null) {
            mAlarmsView.showNoAlarms();
        }
    }
}
