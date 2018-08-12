package com.crypt.adclock.addeditalarm.dialogs.ringtonepicker;

import android.net.Uri;
import android.support.v4.app.FragmentManager;

import javax.inject.Inject;

public class RingtonePickerPresenter implements
        RingtoneDialogContract.Presenter {

    private RingtoneDialogContract.View.OnRingtoneSelectedListener mListener;
    private RingtoneDialogContract.View mView;
    private FragmentManager mFragmentManager;

    @Inject
    public RingtonePickerPresenter(FragmentManager fragmentManager,
                                   RingtoneDialogContract.View.OnRingtoneSelectedListener l) {
        mFragmentManager = fragmentManager;
        mListener = l;
    }

    @Override
    public void show(Uri initialUri, String tag) {
        RingtonePickerDialog dialog = RingtonePickerDialog.newInstance(mListener, initialUri);
        dialog.show(mFragmentManager, tag);
    }

    @Override
    public void takeView(RingtoneDialogContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
