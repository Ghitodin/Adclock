package com.crypt.adclock.addeditalarm;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.crypt.adclock.addeditalarm.dialogs.editlabel.EditLabelContract;
import com.crypt.adclock.addeditalarm.dialogs.editlabel.EditLabelPresenter;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.RepeatType;
import com.crypt.adclock.data.source.AlarmsDataSource;
import com.crypt.adclock.data.source.AlarmsRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

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

    @Inject
    AddEditAlarmPresenter(@NonNull AlarmsRepository repository, @NonNull Context context) {
        mRepository = repository;
        mContext = context;
        createOrLoadAlarm();
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
    public void setRingtone(String stringUri) {
        mAlarm.setRingtone(stringUri);

        // Parsing and displaying ringtone name
        Uri uri = Uri.parse(stringUri);
        String ringtoneName = RingtoneManager
                .getRingtone(mContext, uri)
                .getTitle(mContext);

        if (mView != null) {
            mView.displayRingtoneName(ringtoneName);
        }
    }

    @Override
    public void setLabel(String label) {
        mAlarm.setTitle(label);
        if (mView != null) {
            mView.displayLabel(label);
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
        if (mView != null)
            mView.showLabelInputDialog(mAlarm.getTitle());
    }

    @Override
    public void pickRingtone() {
        if (mView != null)
            mView.showPickRingtoneDialog();
    }

    @Override
    public void takeView(AddEditAlarmContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    private boolean isNewAlarm() {
        return mAlarmId == null;
    }

    private Uri getSelectedRingtoneUri() {
        Uri selectedRingtoneUri;
        String ringtone = "";//mAlarm.getRingtone();
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

    private void createOrLoadAlarm() {
        if (isNewAlarm()) {

            mAlarm = new Alarm("Kek", new Time(Calendar.getInstance().getTimeInMillis()), 0,
                    1, true,
                    RingtoneManager.getActualDefaultRingtoneUri
                            (mContext, RingtoneManager.TYPE_ALARM).toString());

        } else {
            // Get from repo by id
        }
    }

}