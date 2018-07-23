package com.crypt.adclock.addeditalarm;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.crypt.adclock.R;
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;


public class AddEditAlarmFragment extends Fragment implements AddEditAlarmContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";

    private OnFragmentInteractionListener mListener;

    public AddEditAlarmFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static AddEditAlarmFragment newInstance() {
        return new AddEditAlarmFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_edit_alarm_fragment, container, false);
        MaterialNumberPicker hoursNumberPicker = root.findViewById(R.id.hoursNumberPicker);
        MaterialNumberPicker minutesNumberPicker = root.findViewById(R.id.minutesNumberPicker);
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
        hoursNumberPicker.setFormatter(timeFormatter);
        minutesNumberPicker.setFormatter(timeFormatter);

        ListView testListView = root.findViewById(R.id.testListView);

        return root;
    }

    @Override
    public void showRepeatSettings() {

    }

    @Override
    public void showRingtoneSettings() {

    }

    @Override
    public void showVibrateSettings() {

    }

    @Override
    public void showDescriptionSettings() {

    }

    @Override
    public void setPresenter(AddEditAlarmContract.Presenter presenter) {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

