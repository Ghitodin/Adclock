package com.crypt.adclock.alarms;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.crypt.adclock.addeditalarm.AddEditAlarmActivity;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.source.AlarmsRepository;

/**
 * Created by Ghito on 08-Mar-18.
 */

public class AlarmsPresenter implements AlarmsContract.Presenter {

    private final AlarmsContract.View mTasksView;

    public AlarmsPresenter(@NonNull AlarmsRepository tasksRepository,
                          @NonNull AlarmsContract.View tasksView) {
        mTasksView = tasksView;

        mTasksView.setPresenter(this);
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
        mTasksView.showAddAlarm();
    }

    @Override
    public void activateAlarm(@NonNull Alarm activeTask) {

    }

    @Override
    public void start() {

    }
}
