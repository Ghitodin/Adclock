package com.crypt.adclock.addeditalarm;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crypt.adclock.addeditalarm.dialogs.editlabel.EditLabelDialog;
import com.crypt.adclock.addeditalarm.dialogs.editlabel.EditLabelPresenter;
import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerDialog;
import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerPresenter;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.RepeatType;

import org.jetbrains.annotations.Nullable;

/**
 * Created by Ghito on 08-Mar-18.
 */

public class AddEditAlarmPresenter implements
        AddEditAlarmContract.Presenter {
    private Alarm mAlarm;
    private AddEditAlarmContract.View mView;
    private Context mContext;

    @Nullable
    private String mAlarmId;

    private RingtonePickerPresenter mRingtonePickerPresenter;
    private EditLabelPresenter mEditLabelPresenter;

    private final String TAG = "AddEditAlarmPresenter";

    AddEditAlarmPresenter(Context context, AddEditAlarmContract.View view, String alarmId) {
        mAlarmId = alarmId;
        mView = view;
        mContext = context;

        createOrLoadAlarm();

        mView.setPresenter(this);
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

    }

    @Override
    public void setVibrateMode(boolean isVibrateOn) {

    }

    @Override
    public void start() {
        if (mView == null)
            return;
        createOrLoadAlarm();

        mView.updateView(mAlarm);
    }

    @Override
    public void editRepeatMode() {
        mView.showRepeatSettingsDialog();
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

        mEditLabelPresenter.show(mAlarm.getTitle(), "test_tag");
    }

    @Override
    public void pickRingtone() {
        mRingtonePickerPresenter = new RingtonePickerPresenter(
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
        else
            selectedRingtoneUri = Uri.parse(ringtone);

        return selectedRingtoneUri;
    }

    private boolean isNewAlarm() {
        return mAlarmId == null;
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