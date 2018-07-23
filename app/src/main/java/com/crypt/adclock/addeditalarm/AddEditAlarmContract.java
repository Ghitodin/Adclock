package com.crypt.adclock.addeditalarm;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;
import com.crypt.adclock.data.RepeatType;

/**
 * Created by Ghito on 08-Mar-18.
 */

public interface AddEditAlarmContract {
    interface View extends BaseView<Presenter> {

        void showRepeatSettings();

        void showRingtoneSettings();

        void showVibrateSettings();

        void showDescriptionSettings();

    }
    interface Presenter extends BasePresenter {

        void saveAlarm(int hours, int minutes, RepeatType repeatType,
                       String ringtone, String description);

    }
}
