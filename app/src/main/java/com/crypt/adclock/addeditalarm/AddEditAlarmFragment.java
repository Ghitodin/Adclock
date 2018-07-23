package com.crypt.adclock.addeditalarm;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.crypt.adclock.R;
import com.crypt.adclock.data.RepeatType;
import com.crypt.adclock.widgets.ClickableTextViewRow;
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;


public class AddEditAlarmFragment extends Fragment implements AddEditAlarmContract.View {

    public static final String ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID";
    private final int AM_INT = 0;
    private final int PM_INT = 1;

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
        MaterialNumberPicker amPmPicker = root.findViewById(R.id.amPmNumber);
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

        hoursNumberPicker.setFormatter(timeFormatter);
        minutesNumberPicker.setFormatter(timeFormatter);
        amPmPicker.setFormatter(amPmFormatter);

        return root;
    }

    @Override
    public void showRepeatMode(RepeatType repeatType) {

    }

    @Override
    public void showRingtoneName(String ringtoneName) {

    }

    @Override
    public void showVibrateMode(boolean isVibrateOn) {

    }

    @Override
    public void showDescription(String description) {

    }

    @Override
    public void setPresenter(AddEditAlarmContract.Presenter presenter) {

    }

    private void showRepeatSettingsDialog() {

    }

    private void showRingtoneSettingsDialog() {

    }

    private void showDescriptionSettingsDialog() {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

