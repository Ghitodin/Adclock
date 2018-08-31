package com.crypt.adclock.alarms;

import android.support.annotation.NonNull;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;
import com.crypt.adclock.data.Alarm;

import java.util.List;

/**
 * Created by Ghito on 08-Mar-18.
 */

public interface AlarmsContract {
    interface View extends BaseView<Presenter> {

        void showAlarms(List<Alarm> alarms);

        void showAddAlarm();

        void showNoAlarms();

        void showAddedAlarmMessage(Alarm newAlarm);

        void showErrorMessage();

    }

    interface Presenter extends BasePresenter<View> {

        void result(int requestCode, int resultCode);

        void loadAlarms();

        void addNewAlarm();

        void activateAlarm(@NonNull Alarm activeTask);

    }
}
