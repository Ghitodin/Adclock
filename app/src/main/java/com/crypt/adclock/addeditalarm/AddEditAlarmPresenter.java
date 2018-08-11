package com.crypt.adclock.addeditalarm;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

    @Nullable
    private String mAlarmId;

    private RingtonePickerPresenter mRingtonePickerPresenter;
    private EditLabelPresenter mEditLabelPresenter;

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
    public void editLabel() {
        mEditLabelPresenter = new EditLabelPresenter(
                ((AppCompatActivity) mContext).getSupportFragmentManager(),
                new EditLabelDialog.OnLabelSetListener() {
                    @Override
                    public void onLabelSet(String label) {
                        mAlarm.setTitle(label);
                        mView.updateView(mAlarm);
                        Log.d(TAG, "Label was set: " + label);
                    }
                }
        );

        //mEditLabelPresenter.show(mAlarm.getTitle(), "test_tag");
        mEditLabelPresenter.show("sds", "test_tag");
    }

    @Override
    public void pickRingtone() {
        mRingtonePickerPresenter = new  RingtonePickerPresenter(
                // TODO: Fix that:
                ((AppCompatActivity) mContext).getSupportFragmentManager(),
                new RingtonePickerDialog.OnRingtoneSelectedListener() {
                    @Override
                    public void onRingtoneSelected(Uri ringtoneUri) {
                        mAlarm.setRingtone(ringtoneUri.toString());
                        mView.updateView(mAlarm);
                    }
                }
        );

        // TODO Implement util for tagging
        mRingtonePickerPresenter.show(getSelectedRingtoneUri(), "test_tag");
    }

    private Uri getSelectedRingtoneUri() {
        Uri selectedRingtoneUri;
        String ringtone = mAlarm.getRingtone();
        if (ringtone.isEmpty())
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
    private boolean isNewAlarm() {
        return mAlarmId == null;
    }

    @Override
    public void takeView(AddEditAlarmContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
    private void createOrLoadAlarm() {
        if (isNewAlarm()) {
            /* Initialize new alarm
                    mAlarm = new Alarm("Kek", new Date(), RepeatType.OnlyOnce,
                    1, true,
                    RingtoneManager.getActualDefaultRingtoneUri
                            (mContext, RingtoneManager.TYPE_ALARM).toString());
            */
        } else {
            // Get from repo by id
        }
    }

}