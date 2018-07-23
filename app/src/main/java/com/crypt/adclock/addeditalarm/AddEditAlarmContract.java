package com.crypt.adclock.addeditalarm;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;
import com.crypt.adclock.data.RepeatType;

/**
 * Created by Ghito on 08-Mar-18.
 */

public interface AddEditAlarmContract {

    interface View extends BaseView<Presenter> {

        void showRepeatMode(RepeatType repeatType);

        void showRingtoneName(String ringtoneName);

        void showVibrateMode(boolean isVibrateOn);

        void showDescription(String description);
    }

    interface Presenter extends BasePresenter {

        void saveAlarm();

        void setHours(int hours);

        void setMinutes(int minutes);

        void setRepeatType(RepeatType repeatType);

        void setRingtone(String ringtone);

        void setDescription(String description);

        void setVibrateMode(boolean isVibrateOn);
    }
}
