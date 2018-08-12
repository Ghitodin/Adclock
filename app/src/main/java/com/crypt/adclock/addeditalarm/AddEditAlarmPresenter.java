package com.crypt.adclock.addeditalarm;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerDialog;
import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerPresenter;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.RepeatType;
import com.crypt.adclock.data.source.AlarmsDataSource;
import com.crypt.adclock.data.source.AlarmsRepository;

import javax.inject.Inject;

/**
 * Created by Ghito on 08-Mar-18.
 */

public class AddEditAlarmPresenter implements
        AddEditAlarmContract.Presenter {
    private Alarm mAlarm;
    @Nullable
    private AddEditAlarmContract.View mView;
    private Context mContext;
    private AlarmsDataSource mRepository;
    @Inject
    FragmentManager mFragmentManager;

    private RingtonePickerPresenter mRingtonePickerPresenter;

    @Inject
    AddEditAlarmPresenter(@NonNull AlarmsRepository repository, @NonNull Context context) {
        mRepository = repository;
        mContext = context;
    }

    @Override
    public void saveAlarm() {

    }

    @Override
    public void setHours(int hours) {

    }

    @Override
    public void setMinutes(int minutes) {

    }

    @Override
    public void setRepeatType(RepeatType repeatType) {

    }

    @Override
    public void setRingtone(String ringtone) {

    }

    @Override
    public void setLabel(String label) {
        if (mView != null) {
            mView.updateView(mAlarm);
        }
    }

    @Override
    public void setVibrateMode(boolean isVibrateOn) {

    }

    @Override
    public void editRepeatMode() {
        if (mView != null) {
            mView.showRepeatSettingsDialog();
        }
    }

    @Override
    public void pickRingtone() {
        mRingtonePickerPresenter = new  RingtonePickerPresenter(
                mFragmentManager,
                new RingtonePickerDialog.OnRingtoneSelectedListener() {
                    @Override
                    public void onRingtoneSelected(Uri ringtoneUri) {
                    }
                }
        );

        // TODO Implement util for tagging
        mRingtonePickerPresenter.show(getSelectedRingtoneUri(), "test_tag");
    }

    private Uri getSelectedRingtoneUri() {
        Uri selectedRingtoneUri;
        // TODO Uncomment
        // String ringtone = mAlarm.getRingtone();
        String ringtone = "";
        if (ringtone.isEmpty()) {
            // If ringtone is not specified, we take the default ringtone.
            selectedRingtoneUri =
                    RingtoneManager.getActualDefaultRingtoneUri(mContext,
                            RingtoneManager.TYPE_ALARM);
        } else {
            selectedRingtoneUri = Uri.parse(ringtone);
        }

        return selectedRingtoneUri;
    }

    @Override
    public void editLabel() {
        // TODO Change hardcoded string to mAlarm.getLabel()
        if (mView != null) {
            mView.showLabelInputDialog("kek");
        }
    }

    @Override
    public void takeView(AddEditAlarmContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
