package com.crypt.adclock.addeditalarm.dialogs.editlabel;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Editable;
import android.util.Log;


public final class EditLabelPresenter implements
        EditLabelContract.Presenter {
    private static final String TAG = "EditLabelPresenter";

    private final EditLabelContract.View.OnLabelSetListener mListener;
    private EditLabelContract.View mView;
    private FragmentManager mFragmentManager;

    public EditLabelPresenter(FragmentManager fragmentManager, EditLabelContract.View.OnLabelSetListener
            listener) {
        mFragmentManager = fragmentManager;
        mListener = listener;
    }

    public void show(CharSequence initialText, String tag) {
        EditLabelDialog dialog = EditLabelDialog.newInstance(mListener, initialText);
        dialog.show(mFragmentManager, tag);
    }

    @Override
    public void takeView(EditLabelContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}