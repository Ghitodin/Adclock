package com.crypt.adclock.addeditalarm;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.source.AlarmsDataSource;
import com.crypt.adclock.data.source.AlarmsRepository;

import java.sql.Time;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Ghito on 08-Mar-18.
 */

final public class AddEditAlarmPresenter implements
        AddEditAlarmContract.Presenter {
    private Alarm mAlarm;
    @Nullable
    private AddEditAlarmContract.View mView;
    private Context mContext;
    private final AlarmsDataSource mRepository;

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
    public void setRingtone(String stringUri) {
        mAlarm.setRingtone(stringUri);

        if (mView != null) {
            mView.displayRingtoneName(getRingtoneNameFromUri(stringUri));
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
    public void onWeekDayClicked(int day, boolean isRepeating) {
        if (mAlarm == null)
            return;

        if (day > mAlarm.getRepeatDays().size())
            return;

        mAlarm.getRepeatDays().add(day, isRepeating);
    }

    @Override
    public void takeView(AddEditAlarmContract.View view) {
        mView = view;
        if (mAlarm == null || mView == null)
            return;

        updateView();
    }

    @Override
    public void dropView() {
        mView = null;
    }

    private String getRingtoneNameFromUri(String stringUri) {
        // Parsing and displaying ringtone name
        Uri uri = Uri.parse(stringUri);
        return RingtoneManager.getRingtone(mContext, uri)
                .getTitle(mContext);
    }

    private boolean isNewAlarm() {
        return mAlarmId == null;
    }

    private void updateView() {
        if (mView != null) {
            mView.displayRepeatingOn(mAlarm.getRepeatDays());
            mView.displayRingtoneName(getRingtoneNameFromUri(mAlarm.getRingtone()));
            mView.displayLabel(mAlarm.getTitle());
        }
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

            ArrayList<Boolean> repeatDays = new ArrayList<>();
            for (int i = 0; i < 7; ++i) {
                repeatDays.add(true);
            }

            mAlarm = new Alarm("Kek",
                    new Time(9, 10, 0),
                    repeatDays,
                    true,
                    RingtoneManager.getActualDefaultRingtoneUri
                            (mContext, RingtoneManager.TYPE_ALARM).toString()
            );

        } else {
            // Get from repo by id
        }
    }

}