package com.crypt.adclock.addeditalarm;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.crypt.adclock.util.ColorFromThemeUtil;
import com.crypt.adclock.util.WeekDays;
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class AddEditAlarmFragment extends DaggerFragment implements
        AddEditAlarmContract.View,
        View.OnClickListener {

    @Inject
    AddEditAlarmContract.Presenter mPresenter;

    @Inject
    RingtonePickerDialog mRingtonePickerDialog;

    @Inject
    EditLabelDialog mEditLabelDialog;

    MaterialNumberPicker mHoursNumberPicker;
    MaterialNumberPicker mMinutesNumberPicker;
    MaterialNumberPicker mAmPmPicker;

    ConstraintLayout mRingtoneItem;
    ConstraintLayout mVibrationItem;
    ConstraintLayout mLabelItem;

    TextView mLabel;
    TextView mRingtoneName;
    SwitchCompat mVibrationSwitch;

    ColorStateList mDayToggleColors;
    ToggleButton[] mDays;

    public static final String EXTRA_EDIT_ALARM_ID = "EDIT_ALARM_ID";
    private final int AM_INT = 0;
    private final int PM_INT = 1;

    private OnFragmentInteractionListener mListener;

    @Inject
    public AddEditAlarmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = getActivity().findViewById(R.id.fabSaveAlarm);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveAlarm(mLabel.getText().toString(), null, null);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_edit_alarm_fragment, container, false);
        mHoursNumberPicker = root.findViewById(R.id.hoursNumberPicker);
        mMinutesNumberPicker = root.findViewById(R.id.minutesNumberPicker);
        mAmPmPicker = root.findViewById(R.id.amPmNumber);

        mRingtoneItem = root.findViewById(R.id.cl_clickable_ringtone_item);
        mVibrationItem = root.findViewById(R.id.cl_clickable_vibro_item);
        mLabelItem = root.findViewById(R.id.cl_clickable_label_item);

        mLabel = root.findViewById(R.id.tv_label);
        mRingtoneName = root.findViewById(R.id.tv_ringtone_name);
        mVibrationSwitch = root.findViewById(R.id.sc_vibro_switch);

        mDays = new ToggleButton[7];
        initColorForToggleStates();
        bindDaysButtons(root);

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
            @Override
            public String format(int value) {
                if (value == AM_INT)
                    return getString(R.string.am);
                else
                    return getString(R.string.pm);
            }
        };

        mHoursNumberPicker.setFormatter(timeFormatter);
        mMinutesNumberPicker.setFormatter(timeFormatter);
        mAmPmPicker.setFormatter(amPmFormatter);

        mRingtoneItem.setOnClickListener(this);
        mLabelItem.setOnClickListener(this);
        mVibrationItem.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_clickable_ringtone_item:
                mPresenter.pickRingtone();
                break;
            case R.id.cl_clickable_label_item:
                mPresenter.editLabel(mLabel.getText().toString());
                break;
            case R.id.cl_clickable_vibro_item:
                mVibrationSwitch.setChecked(!mVibrationSwitch.isChecked());
                break;
        }
    }

    @Override
    public void displayRingtoneName(String ringtoneName) {
        mRingtoneName.setText(ringtoneName);
    }

    @Override
    public void displayLabel(String label) {
        // Displaying label name
        mLabel.setText(label);
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

    @Override
    public void displayRepeatingOn(ArrayList<Boolean> repeatOn) {
        for (int i = 0; i < repeatOn.size(); i++)
            mDays[i].setChecked(repeatOn.get(i));
    }

    // TODO Think about it
    private void initColorForToggleStates() {
        //We need to define color for "on"-state
        int[][] states = {
                /*item 1*/{/*states*/android.R.attr.state_checked},
                /*item 2*/{/*states*/}
        };

        int[] dayToggleColors = {
                /*item 1*/ColorFromThemeUtil.getTextColorFromThemeAttr
                (getContext(), R.attr.colorAccent),
                /*item 2*/ColorFromThemeUtil.getTextColorFromThemeAttr
                (getContext(), android.R.attr.textColorHint)
        };

        mDayToggleColors = new ColorStateList(states, dayToggleColors);
    }

    private void bindDaysButtons(View root) {
        mDays[0] = root.findViewById(R.id.day1);
        mDays[1] = root.findViewById(R.id.day2);
        mDays[2] = root.findViewById(R.id.day3);
        mDays[3] = root.findViewById(R.id.day4);
        mDays[4] = root.findViewById(R.id.day5);
        mDays[5] = root.findViewById(R.id.day6);
        mDays[6] = root.findViewById(R.id.day7);

        for (int i = 0; i < mDays.length; i++) {
            mDays[i].setTextColor(mDayToggleColors);
            String label = WeekDays.getLabel(i);
            mDays[i].setTextOn(label);
            mDays[i].setTextOff(label);

            //By default we display buttons as inactive
            mDays[i].setChecked(false);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}

