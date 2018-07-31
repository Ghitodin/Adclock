package com.crypt.adclock.addeditalarm;

import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.data.RepeatType;

/**
 * Created by Ghito on 08-Mar-18.
 */

public class AddEditAlarmPresenter implements
        AddEditAlarmContract.Presenter {
    private Alarm mAlarm;
    private AddEditAlarmContract.View mView;

    AddEditAlarmPresenter(AddEditAlarmContract.View view) {
        mView = view;
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
        mView.updateView(mAlarm);
    }

    @Override
    public void setVibrateMode(boolean isVibrateOn) {

    }

    @Override
    public void start() {
        if (mView == null)
            return;

        mView.updateView(mAlarm);
    }

    @Override
    public void editRepeatMode() {
        mView.showRepeatSettingsDialog();
    }

    @Override
    public void editRingtone() {
        mView.showRingtoneSettingsDialog();
    }

    @Override
    public void editLabel() {
        // TODO Change hardcoded string to mAlarm.getLabel()
        mView.showLabelInputDialog("kek");
    }

}
