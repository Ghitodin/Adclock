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

        void showAlarms(List<Alarm> tasks);

        void showAddAlarm();

        void showNoAlarms();

    }

    interface Presenter extends BasePresenter<View> {

        void result(int requestCode, int resultCode);

        void loadAlarms(boolean forceUpdate);

        void addNewAlarm();

        void activateAlarm(@NonNull Alarm activeTask);

    }
}
