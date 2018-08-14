package com.crypt.adclock.addeditalarm;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;

import java.util.ArrayList;

/**
 * Created by Ghito on 08-Mar-18.
 */

public interface AddEditAlarmContract {

    interface View extends BaseView<Presenter> {

        void displayRingtoneName(String ringtoneName);

        void displayLabel(String description);

        void showRepeatSettingsDialog();

        void showLabelInputDialog(String currentLabel);

        void showPickRingtoneDialog();

    }

    interface Presenter extends BasePresenter<View> {

        void saveAlarm();

        void setHours(int hours);

        void setMinutes(int minutes);

        void setRepeatDays(ArrayList<Boolean> repeatType);

        void setRingtone(String ringtone);

        void setLabel(String label);

        void setVibrateMode(boolean isVibrateOn);

        void editRepeatMode();

        void editLabel();

        void pickRingtone();
    }
}
