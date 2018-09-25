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

        interface AlarmItemListener {

            void onActivateClick(Alarm activatedAlarm);

            void onDeactivateClick(Alarm deactivatedAlarm);

            void onAlarmLongPressed(Alarm longPressedAlarm);

            void onAlarmClicked(Alarm clickedAlarm);

            void onAlarmsRemoved(List<Alarm> alarms);

            }

        void showAlarms(List<Alarm> alarms);

        void showEditAlarm(Alarm alarm);

        void showAddAlarm();

        void showNoAlarms();

        void showAddedAlarmMessage(Alarm newAlarm);

        void clearSelection();

        void showErrorMessage();

    }

    interface Presenter extends BasePresenter<View> {

        void result(int requestCode, int resultCode);

        void loadAlarms();

        void addNewAlarm();

        void activateAlarm(@NonNull Alarm activeTask);

        void editAlarm(Alarm alarm);

        void removeAlarms(List<Alarm> alarms);

    }
}
