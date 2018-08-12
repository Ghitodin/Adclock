package com.crypt.adclock.addeditalarm.dialogs.editlabel;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;

public interface EditLabelContract {

    interface View extends BaseView<Presenter> {

        interface OnLabelSetListener {
            void onLabelSet(String str);

        }

        void setOnLabelSetListener(OnLabelSetListener l);

    }

    interface Presenter extends BasePresenter<View> {
        void show(CharSequence initialText, String tag);
    }

}