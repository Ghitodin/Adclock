package com.crypt.adclock.addeditalarm.dialogs.ringtonepicker;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.crypt.adclock.addeditalarm.dialogs.BaseAlertDialogPresenter;


public final class EditLabelPresenter extends BaseAlertDialogPresenter<EditLabelDialog> {
    private static final String TAG = "EditLabelPresenter";

    private final EditLabelDialog.OnLabelSetListener mListener;

    public EditLabelPresenter(FragmentManager fragmentManager, EditLabelDialog.OnLabelSetListener listener) {
        super(fragmentManager);
        mListener = listener;
    }

    public void show(CharSequence initialText, String tag) {
        EditLabelDialog dialog = EditLabelDialog.newInstance(mListener, initialText);
        show(dialog, tag);
    }

    @Override
    public void tryRestoreCallback(String tag) {
        EditLabelDialog labelDialog = findDialog(tag);
        if (labelDialog != null) {
            Log.i(TAG, "Restoring add label callback");
            labelDialog.setOnLabelSetListener(mListener);
        }
    }

}