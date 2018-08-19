package com.crypt.adclock.addeditalarm;

import android.net.Uri;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Ghito on 08-Mar-18.
 */

public interface AddEditAlarmContract {

    interface View extends BaseView<Presenter> {

        void displayRingtoneName(String ringtoneName);

        void displayLabel(String description);

        void displayRepeatingOn(ArrayList<Boolean> repeatDays);

        void showLabelInputDialog(String currentLabel);

        void showPickRingtoneDialog(Uri ringtoneUri);

        void finishAddEdit();

        boolean isActive();

    }

    interface Presenter extends BasePresenter<View> {

        void saveAlarm(String title, Time time, ArrayList<Boolean> repeatDays);

        void setHours(int hours);

        void setMinutes(int minutes);

        void setRingtone(Uri ringtone);

        void setLabel(String label);

        void setVibrateMode(boolean isVibrateOn);

        void editRepeatMode();

        void editLabel(String currentLabel);

        void pickRingtone();

        boolean isNeedToLoadData();

        void createOrLoadAlarm();

    }
}
