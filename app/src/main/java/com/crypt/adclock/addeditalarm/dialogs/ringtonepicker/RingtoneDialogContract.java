package com.crypt.adclock.addeditalarm.dialogs.ringtonepicker;

import android.net.Uri;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;

public interface RingtoneDialogContract {

    interface View extends BaseView<Presenter> {

        interface OnRingtoneSelectedListener {
            void onRingtoneSelected(Uri ringtoneUri);
        }

        void setOnRingtoneSelectedListener(OnRingtoneSelectedListener l);

    }

    interface Presenter extends BasePresenter<View> {
        void show(Uri initialUri, String tag);
    }

}
