package com.crypt.adclock.addeditalarm.dialogs.ringtonepicker;

import android.net.Uri;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

public class RingtonePickerPresenter implements
        RingtonePickerContract.Presenter {

    private RingtonePickerContract.View.OnRingtoneSelectedListener mListener;
    private RingtonePickerContract.View mView;
    private FragmentManager mFragmentManager;

    @Inject
    public RingtonePickerPresenter(FragmentManager fragmentManager,
                                   RingtonePickerContract.View.OnRingtoneSelectedListener l) {
        mFragmentManager = fragmentManager;
        mListener = l;
    }

    @Override
    public void show(Uri initialUri, String tag) {
        RingtonePickerDialog dialog = RingtonePickerDialog.newInstance(mListener, initialUri);
        dialog.show(mFragmentManager, tag);
    }

    @Override
    public void takeView(RingtonePickerContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
