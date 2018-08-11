package com.crypt.adclock.alarms;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crypt.adclock.addeditalarm.AddEditAlarmActivity;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.source.AlarmsDataSource;
import com.crypt.adclock.data.source.AlarmsRepository;
import com.crypt.adclock.di.ActivityScoped;

import javax.inject.Inject;

/**
 * Created by Ghito on 08-Mar-18.
 */
@ActivityScoped
public class AlarmsPresenter implements AlarmsContract.Presenter {
    @Nullable
    private AlarmsContract.View mAlarmsView;
    private final AlarmsDataSource mRepository;

    @Inject
    AlarmsPresenter(@NonNull AlarmsRepository repository) {
        mRepository = repository;
    }

    @Override
    public void result(int requestCode, int resultCode) {
        // If a task was successfully added, show snackbar
        if (AddEditAlarmActivity.REQUEST_ADD_TASK == requestCode && Activity.RESULT_OK == resultCode) {

        }
    }

    @Override
    public void loadAlarms(boolean forceUpdate) {

    }

    @Override
    public void addNewAlarm() {
        if (mAlarmsView != null) {
            mAlarmsView.showAddAlarm();
        }
    }

    @Override
    public void activateAlarm(@NonNull Alarm activeTask) {

    }

    @Override
    public void takeView(AlarmsContract.View view) {
        mAlarmsView = view;
        // TODO: Load alarms from the repository
    }

    @Override
    public void dropView() {
        mAlarmsView = null;
    }
}
