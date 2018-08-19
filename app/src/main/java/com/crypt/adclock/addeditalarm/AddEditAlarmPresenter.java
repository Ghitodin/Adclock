package com.crypt.adclock.addeditalarm;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crypt.adclock.R;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.source.AlarmsDataSource;
import com.crypt.adclock.data.source.AlarmsRepository;

import org.jetbrains.annotations.Contract;

import java.sql.Time;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Ghito on 08-Mar-18.
 */

final public class AddEditAlarmPresenter implements
        AddEditAlarmContract.Presenter, AlarmsDataSource.LoadAlarmCallback {
    @Nullable
    private AddEditAlarmContract.View mView;
    private Context mContext;
    private final AlarmsDataSource mRepository;
    private Lazy<Boolean> mIsNeedToLoadData;
    private boolean mIsAlarmActive = true;
    private Uri mSelectedRingtoneUri;

    @Nullable
    private String mAlarmId;

    @Inject
    AddEditAlarmPresenter(@Nullable String alarmId, @NonNull AlarmsRepository repository,
                          Lazy<Boolean> isNeedToLoadData, @NonNull Context context) {
        mAlarmId = alarmId;
        mRepository = repository;
        mContext = context;
        mIsNeedToLoadData = isNeedToLoadData;
    }

    @Override
    public void saveAlarm(String title, Time time, ArrayList<Boolean> repeatDays) {
        if (title == null || time == null || repeatDays == null || mSelectedRingtoneUri == null) {
            return; // Something wrong
        }

        if (isNewAlarm()) {
            Alarm newAlarm = new Alarm(title, time, repeatDays, mIsAlarmActive,
                    mSelectedRingtoneUri);
            mRepository.save(newAlarm);
        } else {
            Alarm editedAlarm = new Alarm(mAlarmId, title, time, repeatDays,
                    mIsAlarmActive, mSelectedRingtoneUri);
            mRepository.update(editedAlarm);
        }
        if (mView != null) {
            mView.finishAddEdit();
        }
    }

    @Override
    public void setHours(int hours) {

    }

    @Override
    public void setMinutes(int minutes) {

    }

    @Override
    public void setRingtone(Uri uri) {
        mSelectedRingtoneUri = uri;
        if (mView != null) {
            mView.displayRingtoneName(getRingtoneNameFromUri(uri));
        }
    }

    @Override
    public void setLabel(String label) {
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
    public void editLabel(String currentLabel) {
        if (mView != null)
            mView.showLabelInputDialog(currentLabel);
    }

    @Override
    public void pickRingtone() {
        if (mView != null)
            mView.showPickRingtoneDialog(mSelectedRingtoneUri);
    }

    @Contract(pure = true)
    @Override
    public boolean isNeedToLoadData() {
        return false;
    }

    @Override
    public void createOrLoadAlarm() {
        if (isNewAlarm()) {
            // create default alarm
            mSelectedRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(mContext,
                    RingtoneManager.TYPE_ALARM);
            ArrayList<Boolean> defaultRepeatDays = new ArrayList<>();
            for (int i = 0; i < 7; ++i) { // days in week
                defaultRepeatDays.add(i != 0 && i != 6);
            }
            updateView(mContext.getResources().getString(R.string.default_alarm_title),
                    defaultRepeatDays,
                    mSelectedRingtoneUri);
            return;
        }

        mRepository.get(mAlarmId, this);
    }

    @Override
    public void takeView(AddEditAlarmContract.View view) {
        mView = view;
        if (mIsNeedToLoadData.get()) {
            createOrLoadAlarm();
        }
    }

    @Override
    public void dropView() {
        mView = null;
    }

    private String getRingtoneNameFromUri(Uri uri) {
        return uri == null ? "" : RingtoneManager.getRingtone(mContext, uri)
                .getTitle(mContext);
    }

    @Contract(pure = true)
    private boolean isNewAlarm() {
        return mAlarmId == null;
    }

    private void updateView(String alarmTitle, ArrayList<Boolean> repeatDays, Uri ringtoneUri) {
        if (mView != null && mView.isActive()) {
            mView.displayRepeatingOn(repeatDays);
            mView.displayRingtoneName(getRingtoneNameFromUri(ringtoneUri));
            mView.displayLabel(alarmTitle);
        }
    }

    @Override
    public void onLoaded(Alarm alarm) {
        updateView(alarm.getTitle(), alarm.getRepeatDays(), alarm.getRingtoneUri());
        mIsAlarmActive = alarm.isActive();
        mSelectedRingtoneUri = alarm.getRingtoneUri();
    }

    @Override
    public void onNotAvailable() {

    }
}