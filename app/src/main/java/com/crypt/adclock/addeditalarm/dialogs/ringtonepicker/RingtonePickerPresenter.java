package com.crypt.adclock.addeditalarm.dialogs.ringtonepicker;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import com.crypt.adclock.addeditalarm.dialogs.BaseAlertDialogPresenter;

public class RingtonePickerPresenter
        extends BaseAlertDialogPresenter<RingtonePickerDialog> {

    private final RingtonePickerDialog.OnRingtoneSelectedListener mListener;

    public RingtonePickerPresenter(FragmentManager fragmentManager,
                                   RingtonePickerDialog.OnRingtoneSelectedListener l) {
        super(fragmentManager);
        mListener = l;
    }

    public void show(Uri initialUri, String tag) {
        RingtonePickerDialog dialog = RingtonePickerDialog.newInstance(mListener, initialUri);
        show(dialog, tag);
    }

    @Override
    public void tryRestoreCallback(String tag) {
        RingtonePickerDialog dialog = findDialog(tag);
        if (dialog != null) {
            dialog.setOnRingtoneSelectedListener(mListener);
        }
    }

}
