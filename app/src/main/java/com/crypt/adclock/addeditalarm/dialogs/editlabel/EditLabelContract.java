package com.crypt.adclock.addeditalarm.dialogs.editlabel;

import com.crypt.adclock.BasePresenter;
import com.crypt.adclock.BaseView;

public interface EditLabelContract {

    interface View extends BaseView<Presenter> {

        void show(String initialText);

    }

    interface Presenter extends BasePresenter<View> {

        interface OnLabelSetListener {

            void onLabelSet(String str);

        }

        void setLabel(String text);

    }

}