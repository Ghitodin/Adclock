package com.crypt.adclock.alarms;

/**
 * Created by Ghito on 08-Mar-18.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.crypt.adclock.R;
import com.crypt.adclock.addeditalarm.AddEditAlarmActivity;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.di.ActivityScoped;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class AlarmsFragment extends DaggerFragment implements AlarmsContract.View {

    @Inject
    AlarmsContract.Presenter mPresenter;

    private AlarmItemListener mListener;
    private AlarmsAdapter mAlarmsAdapter;

    @BindView(R.id.alarms_linear_layout)
    LinearLayout mAlarmsView;

    @BindView(R.id.alarms_list)
    RecyclerView mAlarmsList;

    @BindView(R.id.no_alarms)
    View mNoAlarmsView;

    @BindView(R.id.no_alarms_icon)
    ImageView mNoAlarmsIcon;

    @BindView(R.id.no_alarms_main)
    TextView noAlarmsMainView;

    @BindView(R.id.no_alarms_add)
    TextView noAlarmsAddView;

    @Inject
    public AlarmsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void setPresenter(@NonNull AlarmsContract.Presenter presenter) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mPresenter.result(requestCode, resultCode);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.alarms_fragment, container, false);
        ButterKnife.bind(this, root);

        this.mAlarmsAdapter = new AlarmsAdapter(new ArrayList<Alarm>(0));
        mAlarmsList.setAdapter(mAlarmsAdapter);
        mAlarmsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAlarmsList.addItemDecoration(new DividerItemDecoration(mAlarmsList.getContext(),
                DividerItemDecoration.VERTICAL));

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.add_alarm_fab);

        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewAlarm();
            }
        });

        return root;
    }

    @Override
    public void showAlarms(@NonNull List<Alarm> alarms) {
        if (alarms.isEmpty()) {
            return;
        }

        mAlarmsAdapter.replaceData(alarms);

        mAlarmsView.setVisibility(View.VISIBLE);
        mNoAlarmsView.setVisibility(View.GONE);
    }

    @Override
    public void showAddAlarm() {
        Intent intent = new Intent(getContext(), AddEditAlarmActivity.class);
        startActivityForResult(intent, AddEditAlarmActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showNoAlarms() {
        showNoAlarmsViews(
                getResources().getString(R.string.no_alarms),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    private int getNextAlarmDaysTo(List<Boolean> alarmDays, int currentDayOfWeek,
                                   boolean isCurrentIncluded) {
        ArrayList<Boolean> twoWeeksAlarmDays = new ArrayList<>(alarmDays);
        twoWeeksAlarmDays.addAll(alarmDays);
        int startDayOfWeek = isCurrentIncluded ? currentDayOfWeek : (currentDayOfWeek + 1);
        int daysToNextAlarm = startDayOfWeek - currentDayOfWeek;

        for (int i = startDayOfWeek; i < twoWeeksAlarmDays.size(); ++i) {
            if (twoWeeksAlarmDays.get(i)) {
                return daysToNextAlarm;
            }
            ++daysToNextAlarm;
        }
        return 0;
    }

    @NonNull
    private Period calculateTimeLeft(@NonNull Alarm alarm) {
        DateTime currentDateTime = DateTime.now();
        DateTime alarmTime = alarm.getTime();
        List<Boolean> repeatDays = alarm.getRepeatDays();
        int compResult = currentDateTime.compareTo(DateTime.now()
                .withHourOfDay(alarmTime.getHourOfDay())
                .withMinuteOfHour(alarmTime.getMinuteOfHour()));

        int currentDayOfWeek = currentDateTime.getDayOfWeek();
        int daysToNextAlarm = getNextAlarmDaysTo(repeatDays, currentDayOfWeek,
                compResult < 0);


        DateTime alarmDateTime = DateTime.now()
                .withHourOfDay(alarmTime.getHourOfDay())
                .withMinuteOfHour(alarmTime.getMinuteOfHour());

        alarmDateTime = alarmDateTime.plusDays(daysToNextAlarm);

        return new Period(currentDateTime, alarmDateTime);
    }

    @Override
    public void showAddedAlarmMessage(Alarm newAlarm) {
        int timeLeftStringResId;
        int daysStringResId;
        final Period timeToNextAlarm = calculateTimeLeft(newAlarm);
        int daysLeft = timeToNextAlarm.getDays();
        if (daysLeft == 0) {
            timeLeftStringResId = R.string.from_now_message;
            daysStringResId = R.string.days;
        } else {
            timeLeftStringResId = R.string.from_now_message_with_days;
            if (daysLeft == 1) {
                daysStringResId = R.string.day;
            } else {
                daysStringResId = R.string.days;
            }
        }

        int hoursStringResId;
        int minutesStringResId;

        int hoursLeft = timeToNextAlarm.getHours();
        int minutesLeft = timeToNextAlarm.getMinutes();
        if (hoursLeft == 1) {
            hoursStringResId = R.string.hour;
        } else {
            hoursStringResId = R.string.hours;
        }
        if (minutesLeft == 1) {
            minutesStringResId = R.string.minute;
        } else {
            minutesStringResId = R.string.minutes;
        }

        String timeLeftResultString;
        if (daysLeft == 0) {
            timeLeftResultString = getString(timeLeftStringResId, hoursLeft, minutesLeft,
                    getString(hoursStringResId), getString(minutesStringResId));
        } else {
            timeLeftResultString = getString(timeLeftStringResId, daysLeft, hoursLeft, minutesLeft,
                    getString(daysStringResId), getString(hoursStringResId),
                    getString(minutesStringResId));
        }
        showMessage(getString(R.string.alarm_set_for, timeLeftResultString));
    }

    @Override
    public void showErrorMessage() {
        showMessage(getString(R.string.error_ocurred));
    }

    private void showMessage(@NonNull String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void showNoAlarmsViews(String mainText, int iconRes, boolean showAddView) {
        mAlarmsView.setVisibility(View.GONE);
        mNoAlarmsView.setVisibility(View.VISIBLE);

        noAlarmsMainView.setText(mainText);
        mNoAlarmsIcon.setImageDrawable(getResources().getDrawable(iconRes));
        noAlarmsAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }


    public interface AlarmItemListener {
        void onActivateClick(Alarm activatedTask);
    }

    static class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> {

        List<Alarm> mAlarms;

        AlarmsAdapter(@NonNull List<Alarm> alarms) {
            mAlarms = alarms;
        }

        void replaceData(List<Alarm> alarms) {
            mAlarms = alarms;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.alarm_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Alarm alarm = mAlarms.get(position);
            String alarmHours = alarm.getTime().getHourOfDay() < 10 ?
                    ("0" + String.valueOf(alarm.getTime().getHourOfDay())) :
                    String.valueOf(alarm.getTime().getHourOfDay());
            String alarmMinutes = alarm.getTime().getMinuteOfHour() < 10 ?
                    ("0" + String.valueOf(alarm.getTime().getMinuteOfHour())) :
                    String.valueOf(alarm.getTime().getMinuteOfHour());
            String time = alarmHours + ":" + alarmMinutes;
            holder.getTimeTextView().setText(time);
            holder.getLabelTextView().setText(alarm.getTitle());
        }

        @Override
        public int getItemCount() {
            return mAlarms.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTimeTextView;
            private final TextView mLabelTextView;
            private final TextView mRepeatDaysTextView;
            private final Switch mIsActiveSwitch;

            ViewHolder(View v) {
                super(v);
                mTimeTextView = v.findViewById(R.id.alarm_time_tw);
                mLabelTextView = v.findViewById(R.id.alarm_label_tw);
                mRepeatDaysTextView = v.findViewById(R.id.alarm_repeat_tw);
                mIsActiveSwitch = v.findViewById(R.id.is_alarm_active_switch);
            }

            public TextView getTimeTextView() {
                return mTimeTextView;
            }

            public TextView getLabelTextView() {
                return mLabelTextView;
            }

            public TextView getRepeatDaysTextView() {
                return mRepeatDaysTextView;
            }

            public Switch getIsActiveSwitch() {
                return mIsActiveSwitch;
            }
        }
    }
}