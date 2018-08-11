package com.crypt.adclock.addeditalarm;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ToggleButton;

import com.crypt.adclock.R;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.di.ActivityScoped;
import com.crypt.adclock.widgets.ClickableSwitchRow;
import com.crypt.adclock.widgets.ClickableTextViewRow;
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class AddEditAlarmFragment extends DaggerFragment implements
        AddEditAlarmContract.View,
        View.OnClickListener {

    @Inject
    AddEditAlarmContract.Presenter mPresenter;

    MaterialNumberPicker mHoursNumberPicker;
    MaterialNumberPicker mMinutesNumberPicker;
    MaterialNumberPicker mAmPmPicker;

    ClickableTextViewRow mRingtoneSettingsRow;
    ClickableSwitchRow mVibroSettingsRow;
    ClickableTextViewRow mLabelSettingsRow;

    ToggleButton mDay1;

    public static final String ARGUMENT_EDIT_ALARM_ID = "EDIT_ALARM_ID";
    private final int AM_INT = 0;
    private final int PM_INT = 1;

    private AddEditAlarmContract.Presenter mPresenter;

    private final int RINGTONE_REQUEST_CODE = 5;

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
                mPresenter.saveAlarm();
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

        mRingtoneSettingsRow = root.findViewById(R.id.clickable_ringtone_item);
        mVibroSettingsRow = root.findViewById(R.id.clickable_switch_vibro_item);
        mLabelSettingsRow = root.findViewById(R.id.clickable_label_item);

        mDay1 = root.findViewById(R.id.day1);

        mDay1.setTextOn("Kok");
        mDay1.setTextOff("Kek");

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

        mRingtoneSettingsRow.setOnClickListener(this);
        mLabelSettingsRow.setOnClickListener(this);

        mDay1.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.dropView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clickable_ringtone_item:
                mPresenter.pickRingtone();
                break;
            case R.id.clickable_label_item:
                mPresenter.editLabel();
                break;
        }
    }

    @Override
    public void updateView(Alarm alarm) {
        // Parsing and displaying ringtone name
        Uri ringtone = Uri.parse(alarm.getRingtone());
        String ringtoneName = RingtoneManager
                .getRingtone(getContext(), ringtone)
                .getTitle(getContext());

        mRingtoneSettingsRow.setText(ringtoneName);

        // Displaying label name
        mLabelSettingsRow.setText(alarm.getTitle());
    }

    @Override
    public void setPresenter(AddEditAlarmContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showRepeatSettingsDialog() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        data.getStringExtra(RingtoneManager.EXTRA_RINGTONE_TITLE);
    }

    @Override
    public void showLabelInputDialog(String currentLabel) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.label_dialog_layout);

        final EditText labelEdit = dialog.findViewById(R.id.label);
        final Button cancelButton = dialog.findViewById(R.id.cancel);
        final Button okButton = dialog.findViewById(R.id.ok);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.setLabel(String.valueOf(labelEdit.getText()));
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

