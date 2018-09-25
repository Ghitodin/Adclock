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
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crypt.adclock.R;
import com.crypt.adclock.addeditalarm.AddEditAlarmActivity;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.di.ActivityScoped;

import org.joda.time.DateTime;
import org.joda.time.Period;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnDragInitiatedListener;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class AlarmsFragment extends DaggerFragment implements AlarmsContract.View {

    @Inject
    AlarmsContract.Presenter mPresenter;

    @Inject
    AlarmsAdapter mAlarmsAdapter;

    SelectionTracker<Long> mSelectionTracker;

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

    private ActionMode actionMode;
    FloatingActionButton mAddAlarmFab;

    @Inject
    public AlarmsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mPresenter.takeView(this);
    }

    @Override
    public void onDestroy() {
        mPresenter.dropView();
        super.onDestroy();
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

        mAlarmsList.setAdapter(mAlarmsAdapter);
        mSelectionTracker = new SelectionTracker.Builder<>(
                "my_selection",
                mAlarmsList,
                new AlarmsAdapter.KeyProvider(),
                new AlarmsAdapter.DetailsLookup(mAlarmsList),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(new AlarmsAdapter.Predicate())
                .withOnItemActivatedListener(new OnItemActivatedListener<Long>() {
                    @Override
                    public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item,
                                                   @NonNull MotionEvent e) {
                        // Ignores the regular click
                        return false;
                    }
                })
                .withOnDragInitiatedListener(new OnDragInitiatedListener() {
                    @Override
                    public boolean onDragInitiated(@NonNull MotionEvent e) {
                        return true;
                    }

                })
                .build();

        if (savedInstanceState != null) {
            mSelectionTracker.onRestoreInstanceState(savedInstanceState);
        }

        mAlarmsAdapter.setSelectionTracker(mSelectionTracker);

        mSelectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onItemStateChanged(@NonNull Object key, boolean selected) {
                super.onItemStateChanged(key, selected);
            }

            @Override
            public void onSelectionRefresh() {
                super.onSelectionRefresh();
            }

            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (mSelectionTracker.hasSelection() && actionMode == null) {
                    actionMode = (getActivity())
                            .startActionMode(new ActionModeCallback(getActivity(),
                                    mSelectionTracker));
                } else if (!mSelectionTracker.hasSelection() && actionMode != null) {
                    actionMode.finish();
                    actionMode = null;
                } else {
                    // Get selection items count (for displaying in app bar, for example)
                    Log.e("KEK", "" + mSelectionTracker.getSelection().size());
                }

                // Get items:
                for (Long aLong : mSelectionTracker.getSelection()) {
                    Log.i("KEK", aLong.toString());
                }

            }

            @Override
            public void onSelectionRestored() {
                super.onSelectionRestored();
            }
        });

        mAlarmsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAlarmsList.addItemDecoration(new DividerItemDecoration(mAlarmsList.getContext(),
                DividerItemDecoration.VERTICAL));

        mAlarmsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mAddAlarmFab.hide();
                } else {
                    mAddAlarmFab.show();
                }
            }
        });

        mAddAlarmFab = getActivity().findViewById(R.id.add_alarm_fab);

        mAddAlarmFab.setImageResource(R.drawable.ic_add);
        mAddAlarmFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addNewAlarm();
            }
        });

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_selections) {
            // Test option menu:
            Toast.makeText(getActivity(), mSelectionTracker.getSelection().toString(), Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
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
    public void showEditAlarm(Alarm alarm) {
        Intent editAlarmIntent = new Intent(getContext(), AddEditAlarmActivity.class);
        editAlarmIntent.putExtra(AlarmsActivity.EXTRA_ALARM_ID, alarm.getId());
        startActivityForResult(editAlarmIntent, AddEditAlarmActivity.REQUEST_EDIT_TASK);
    }

    @Override
    public void showAddAlarm() {
        Intent addAlarmIntent = new Intent(getContext(), AddEditAlarmActivity.class);
        startActivityForResult(addAlarmIntent, AddEditAlarmActivity.REQUEST_ADD_TASK);
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
    public void clearSelection() {
        mSelectionTracker.clearSelection();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mSelectionTracker.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
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
}