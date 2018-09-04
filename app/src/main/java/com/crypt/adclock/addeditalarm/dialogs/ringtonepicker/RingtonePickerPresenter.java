package com.crypt.adclock.addeditalarm.dialogs.ringtonepicker;

import android.net.Uri;

import javax.inject.Inject;

public class RingtonePickerPresenter implements
        RingtonePickerContract.Presenter {

    private RingtonePickerContract.View mView;
    RingtonePickerContract.Presenter.OnRingtoneSelectedListener mListener;

    @Inject
    public RingtonePickerPresenter(RingtonePickerContract.Presenter.OnRingtoneSelectedListener
                                               listener) {
        mListener = listener;
    }

    @Override
    public void takeView(RingtonePickerContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void selectRingtone(Uri uri) {
        mListener.onRingtoneSelected(uri);
    }
}
