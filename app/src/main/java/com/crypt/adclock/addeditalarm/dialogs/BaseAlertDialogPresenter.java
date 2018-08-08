package com.crypt.adclock.addeditalarm.dialogs;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;


public abstract class BaseAlertDialogPresenter<T extends DialogFragment> {

    private final FragmentManager mFragmentManager;

    public abstract void tryRestoreCallback(String tag);

    public BaseAlertDialogPresenter(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    /**
     * Shows the dialog with the given tag.
     */
    protected final void show(T dialog, String tag) {
        dialog.show(mFragmentManager, tag);
    }

    /**
     * Tries to find the dialog in our FragmentManager with the provided tag.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    protected final T findDialog(String tag) {
        return (T) mFragmentManager.findFragmentByTag(tag);
    }

}
