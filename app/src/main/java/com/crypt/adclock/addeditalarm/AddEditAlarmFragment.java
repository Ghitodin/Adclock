package com.crypt.adclock.addeditalarm;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.crypt.adclock.R;
import com.crypt.adclock.addeditalarm.dialogs.editlabel.EditLabelDialog;
import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerDialog;
import com.crypt.adclock.di.ActivityScoped;
import com.crypt.adclock.util.TimeFormat;
import com.crypt.adclock.util.WeekDays;
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

import org.joda.time.DateTime;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class AddEditAlarmFragment extends DaggerFragment implements
        AddEditAlarmContract.View,
        AddEditAlarmActivity.OnFloatingButtonClickedListener {

    @Inject
    AddEditAlarmContract.Presenter mPresenter;

    @Inject
    RingtonePickerDialog mRingtonePickerDialog;

    @Inject
    EditLabelDialog mEditLabelDialog;

    @BindView(R.id.hoursNumberPicker)
    MaterialNumberPicker mHoursNumberPicker;
    @BindView(R.id.minutesNumberPicker)
    MaterialNumberPicker mMinutesNumberPicker;
    @BindView(R.id.amPmNumber)
    MaterialNumberPicker mAmPmPicker;

    @BindView(R.id.cl_clickable_ringtone_item)
    ConstraintLayout mRingtoneItem;
    @BindView(R.id.cl_clickable_vibrate_item)
    ConstraintLayout mVibrationItem;
    @BindView(R.id.cl_clickable_label_item)
    ConstraintLayout mLabelItem;

    @BindView(R.id.tv_label)
    TextView mLabel;
    @BindView(R.id.tv_ringtone_name)
    TextView mRingtoneName;
    @BindView(R.id.sc_vibro_switch)
    SwitchCompat mVibrationSwitch;

    @BindViews({R.id.day1, R.id.day2, R.id.day3, R.id.day4, R.id.day5, R.id.day6, R.id.day7})
    ToggleButton[] mRepeatDayButtons;


    public static final String EXTRA_EDIT_ALARM_ID = "EDIT_ALARM_ID";
    private final int AM_INT = 0;
    private final int PM_INT = 1;

    private OnFragmentInteractionListener mListener;

    @Inject
    public AddEditAlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_edit_alarm_fragment, container, false);
        ButterKnife.bind(this, root);

        NumberPicker.Formatter timeFormatter = new NumberPicker.Formatter() {
            @NonNull
            @Override
            public String format(int value) {
                if (value < 10) {
                    return "0" + value;
                }
                return String.valueOf(value);
            }
        };
        NumberPicker.Formatter amPmFormatter = new NumberPicker.Formatter() {
            @NonNull
            @Override
            public String format(int value) {
                if (value == AM_INT)
                    return getString(R.string.am);
                else
                    return getString(R.string.pm);
            }
        };

        setDayNamesForToggles();

        mHoursNumberPicker.setFormatter(timeFormatter);
        mMinutesNumberPicker.setFormatter(timeFormatter);
        mAmPmPicker.setFormatter(amPmFormatter);

        mHoursNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                boolean isAm = (mAmPmPicker.getValue() == AM_INT);
                int base24hours = TimeFormat.base12toBase24Hours(newVal, isAm);
                mPresenter.setHours(base24hours);
            }
        });

        mMinutesNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mPresenter.setMinutes(newVal);
            }
        });

        mAmPmPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                boolean isAm = (newVal == AM_INT);
                int base12hours = mHoursNumberPicker.getValue();
                int base24hours = TimeFormat.base12toBase24Hours(base12hours, isAm);
                mPresenter.setHours(base24hours);
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.takeView(this);
    }

    @Override
    public void onPause() {
        mPresenter.dropView();
        super.onPause();
    }

    @OnClick({R.id.day1, R.id.day2, R.id.day3, R.id.day4, R.id.day5, R.id.day6, R.id.day7})
    void onDayToggled() {
        ArrayList<Boolean> repeatDays = new ArrayList<>();
        for (ToggleButton button : mRepeatDayButtons) {
            repeatDays.add(button.isChecked());
        }
        mPresenter.setRepeatingDays(repeatDays);
    }

    @OnClick(R.id.cl_clickable_ringtone_item)
    void onRingtoneClick() {
        mPresenter.pickRingtone();
    }

    @OnClick(R.id.cl_clickable_label_item)
    void onLabelClicked() {
        mPresenter.editLabel();
    }

    @OnClick(R.id.cl_clickable_vibrate_item)
    void onVibrateModeClicked() {
        mVibrationSwitch.setChecked(!mVibrationSwitch.isChecked());
    }

    @OnCheckedChanged(R.id.sc_vibro_switch)
    void onVibrateCheckedChanged(SwitchCompat button) {
        // Prevents Android issue with automatic call this method
        if (mVibrationSwitch.isPressed() || mVibrationItem.isPressed()) {
            mPresenter.setVibrateMode(button.isChecked());
        }
    }

    @Override
    public void onSaveFabClicked() {
        mPresenter.saveAlarm();
    }

    @Override
    public void displayRingtoneName(String ringtoneName) {
        mRingtoneName.setText(ringtoneName);
    }

    @Override
    public void displayLabel(String label) {
        mLabel.setText(label);
    }

    @Override
    public void displayRepeatingDays(ArrayList<Boolean> repeatDays) {
        for (int i = 0; i < repeatDays.size(); i++) {
            boolean shouldBeChecked = repeatDays.get(i);
            boolean isCheckedInView = mRepeatDayButtons[i].isChecked();

            if (isCheckedInView != shouldBeChecked) {
                mRepeatDayButtons[i].setChecked(shouldBeChecked);
            }
        }
    }

    @Override
    public void displayTime(DateTime time) {
        int hours24based = time.getHourOfDay();
        int hours12based = TimeFormat.base24toBase12hours(hours24based);
        int amOrPm = (TimeFormat.isAm(hours24based) ? AM_INT : PM_INT);
        mHoursNumberPicker.setValue(hours12based);
        mMinutesNumberPicker.setValue(time.getMinuteOfHour());
        mAmPmPicker.setValue(amOrPm);
    }

    @Override
    public void displayVibrateMode(boolean isEnabledInPresenter) {
        boolean isEnabledInView = mVibrationSwitch.isChecked();
        if (isEnabledInView != isEnabledInPresenter) {
            mVibrationSwitch.setChecked(isEnabledInPresenter);
        }
    }

    @Override
    public void setPresenter(AddEditAlarmContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        data.getStringExtra(RingtoneManager.EXTRA_RINGTONE_TITLE);
    }

    @Override
    public void showLabelInputDialog(String currentLabel) {
        mEditLabelDialog.show(currentLabel);
    }

    @Override
    public void showPickRingtoneDialog(Uri ringtoneUri) {
        mRingtonePickerDialog.show(ringtoneUri);
    }

    @Override
    public void finishAddEdit() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    private void setDayNamesForToggles() {
        String nameOfTheDay;
        for (int i = 0; i < mRepeatDayButtons.length; i++) {
            nameOfTheDay = WeekDays.getDayShortName(i);
            mRepeatDayButtons[i].setText(nameOfTheDay);
            mRepeatDayButtons[i].setTextOn(nameOfTheDay);
            mRepeatDayButtons[i].setTextOff(nameOfTheDay);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
