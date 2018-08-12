package com.crypt.adclock.addeditalarm.dialogs.ringtonepicker;

import android.net.Uri;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;

public interface RingtonePickerContract {

    interface View extends BaseView<Presenter> {

        void show();

    }

    interface Presenter extends BasePresenter<View> {

        interface OnRingtoneSelectedListener {

            void onRingtoneSelected(Uri ringtoneUri);

        }

        void selectRingtone(Uri uri);

    }

}