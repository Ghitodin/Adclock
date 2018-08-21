package com.crypt.adclock.addeditalarm;

import android.net.Uri;

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

        void displayRepeatingDays(ArrayList<Boolean> repeatDays);

        void displayVibroMode(boolean isEnabled);

        void showLabelInputDialog(String currentLabel);

        void showPickRingtoneDialog(Uri ringtoneUri);

        void finishAddEdit();

        boolean isActive();

    }

    interface Presenter extends BasePresenter<View> {

        void saveAlarm();

        void setHours(int hours);

        void setMinutes(int minutes);

        void setRingtone(Uri ringtone);

        void setLabel(String label);

        void setVibrateMode(boolean isVibrateOn);

        void setRepeatingDays(ArrayList<Boolean> week);

        void editLabel();

        void pickRingtone();

        boolean isNeedToLoadData();

        void createOrLoadAlarm();

    }
}
