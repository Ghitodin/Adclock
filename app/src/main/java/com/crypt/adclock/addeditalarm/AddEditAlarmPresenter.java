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
import java.util.GregorianCalendar;

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
    private Alarm mAlarm;

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
    public void saveAlarm() {
        if (isNewAlarm()) {
            mRepository.save(mAlarm);
        } else {
            mRepository.update(mAlarm);
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
        mAlarm.setRingtoneUri(uri);
        if (mView != null) {
            mView.displayRingtoneName(getRingtoneNameFromUri(uri));
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
            mView.showPickRingtoneDialog(mAlarm.getRingtoneUri());
    }

    @Contract(pure = true)
    @Override
    public boolean isNeedToLoadData() {
        return false;
    }

    @NonNull
    private Alarm getDefaultAlarm() {
        String defaultAlarmTitle = mContext.getResources()
                .getString(R.string.default_alarm_title);

        ArrayList<Boolean> defaultAlarmRepeatDays = new ArrayList<>();
        for (int i = 0; i < 7; ++i) { // days in week
            defaultAlarmRepeatDays.add(i != 0 && i != 6);
        }

        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(mContext,
                RingtoneManager.TYPE_ALARM);

        updateView(defaultAlarmTitle,
                defaultAlarmRepeatDays,
                defaultRingtoneUri);

        long currentTime = new GregorianCalendar().getTime().getTime();
        Time defaultAlarmTime = new Time(currentTime);
        return new Alarm(defaultAlarmTitle, defaultAlarmTime, defaultAlarmRepeatDays,
                true, defaultRingtoneUri);
    }

    @Override
    public void createOrLoadAlarm() {
        if (isNewAlarm()) {
            onLoaded(getDefaultAlarm());
        } else {
            mRepository.get(mAlarmId, this);
        }
    }

    @Override
    public void takeView(AddEditAlarmContract.View view) {
        mView = view;
        if (mIsNeedToLoadData.get()) {
            createOrLoadAlarm();
        } else {
            mRepository.restoreRetained(this);
        }
    }

    @Override
    public void dropView() {
        mRepository.retain(mAlarm);
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
        mAlarm = alarm;
    }

    @Override
    public void onNotAvailable() {

    }
}