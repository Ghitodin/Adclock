package com.crypt.adclock.addeditalarm.dialogs.editlabel;


import javax.inject.Inject;


public final class EditLabelPresenter implements
        EditLabelContract.Presenter {
    private static final String TAG = "EditLabelPresenter";

    private final EditLabelContract.Presenter.OnLabelSetListener mListener;
    private EditLabelContract.View mView;

    @Inject
    public EditLabelPresenter(EditLabelContract.Presenter.OnLabelSetListener listener) {
        mListener = listener;
    }

    @Override
    public void takeView(EditLabelContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    @Override
    public void setLabel(String label) {
        if (mListener != null)
            mListener.onLabelSet(label);
    }

}