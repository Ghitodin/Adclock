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
import org.joda.time.DateTime;

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
    private Lazy<Boolean> mIsNeedToLoadDataLazy;
    private boolean mIsNeedToLoadData;
    @Nullable
    private Alarm mAlarm;

    @Nullable
    private String mAlarmId;

    @Inject
    AddEditAlarmPresenter(@Nullable String alarmId, @NonNull AlarmsRepository repository,
                          Lazy<Boolean> isNeedToLoadData, @NonNull Context context) {
        mAlarmId = alarmId;
        mRepository = repository;
        mContext = context;
        mIsNeedToLoadDataLazy = isNeedToLoadData;
    }

    @Override
    public void saveAlarm() {
        if (mAlarm == null) {
            return;
        }

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
        if (mAlarm == null) {
            return;
        }

        final DateTime currentAlarmTime = mAlarm.getTime();
        final DateTime newTime = new DateTime()
                .withHourOfDay(hours)
                .withMinuteOfHour(currentAlarmTime.getMinuteOfHour());

        mAlarm.setTime(newTime);
        if (mView != null) {
            mView.displayTime(newTime);
        }
    }

    @Override
    public void setMinutes(int minutes) {
        if (mAlarm == null) {
            return;
        }

        final DateTime currentAlarmTime = mAlarm.getTime();
        final DateTime newTime = new DateTime()
                .withHourOfDay(currentAlarmTime.getHourOfDay())
                .withMinuteOfHour(minutes);

        mAlarm.setTime(newTime);
        if (mView != null) {
            mView.displayTime(newTime);
        }
    }

    @Override
    public void setRingtone(Uri uri) {
        if (mAlarm == null) {
            return;
        }

        mAlarm.setRingtoneUri(uri);
        if (mView != null) {
            mView.displayRingtoneName(getRingtoneNameFromUri(uri));
        }
    }

    @Override
    public void setLabel(String label) {
        if (mAlarm == null) {
            return;
        }

        mAlarm.setTitle(label);
        if (mView != null) {
            mView.displayLabel(mAlarm.getTitle());
        }
    }

    @Override
    public void setVibrateMode(boolean isVibrateOn) {
        if (mAlarm == null) {
            return;
        }

        mAlarm.setVibrateEnabled(isVibrateOn);
        if (mView != null) {
            mView.displayVibrateMode(mAlarm.isVibrateEnabled());
        }
    }

    @Override
    public void setRepeatingDays(ArrayList<Boolean> week) {
        if (mAlarm == null) {
            return;
        }

        mAlarm.setRepeatDays(week);
        if (mView != null) {
            mView.displayRepeatingDays(mAlarm.getRepeatDays());
        }
    }

    @Override
    public void editLabel() {
        if (mAlarm == null) {
            return;
        }

        if (mView != null) {
            mView.showLabelInputDialog(mAlarm.getTitle());
        }
    }

    @Override
    public void pickRingtone() {
        if (mAlarm == null) {
            return;
        }

        if (mView != null) {
            mView.showPickRingtoneDialog(mAlarm.getRingtoneUri());
        }
    }

    @Contract(pure = true)
    @Override
    public boolean isNeedToLoadData() {
        return mIsNeedToLoadData;
    }

    @NonNull
    private Alarm getDefaultAlarm() {
        String defaultAlarmTitle = mContext.getResources()
                .getString(R.string.default_alarm_title);

        final ArrayList<Boolean> defaultAlarmRepeatDays = new ArrayList<>();
        for (int i = 0; i < 7; ++i) { // days in week
            defaultAlarmRepeatDays.add(i != 0 && i != 6);
        }

        final Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(mContext,
                RingtoneManager.TYPE_ALARM);

        final boolean defaultVibrateMod = true;

        final DateTime defaultAlarmTime = DateTime.now();

        return new Alarm(defaultAlarmTitle, defaultAlarmTime, defaultAlarmRepeatDays,
                true, defaultRingtoneUri, defaultVibrateMod);
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
        mIsNeedToLoadData = mIsNeedToLoadDataLazy.get();
        if (mIsNeedToLoadData) {
            createOrLoadAlarm();
        } else {
            mRepository.restoreRetained(this);
        }
    }

    @Override
    public void dropView() {
        if (mAlarm != null) {
            mRepository.retain(mAlarm);
        }
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

    private void updateView(String alarmTitle, ArrayList<Boolean> repeatDays, Uri ringtoneUri,
                            boolean isVibrateEnabled, DateTime alarmTime) {
        if (mView != null && mView.isActive()) {
            mView.displayRepeatingDays(repeatDays);
            mView.displayRingtoneName(getRingtoneNameFromUri(ringtoneUri));
            mView.displayLabel(alarmTitle);
            mView.displayVibrateMode(isVibrateEnabled);
            mView.displayTime(alarmTime);
        }
    }

    @Override
    public void onLoaded(Alarm alarm) {
        mAlarm = alarm;
        updateView(mAlarm.getTitle(), mAlarm.getRepeatDays(), mAlarm.getRingtoneUri(),
                mAlarm.isVibrateEnabled(), mAlarm.getTime());
        mIsNeedToLoadData = false;
    }

    @Override
    public void onNotAvailable() {

    }
}