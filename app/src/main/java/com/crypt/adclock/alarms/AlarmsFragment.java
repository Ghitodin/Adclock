package com.crypt.adclock.alarms;

/**
 * Created by Ghito on 08-Mar-18.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.crypt.adclock.R;
import com.crypt.adclock.addeditalarm.AddEditAlarmActivity;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.di.ActivityScoped;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

import static android.support.v4.util.Preconditions.checkNotNull;

@ActivityScoped
public class AlarmsFragment extends DaggerFragment implements AlarmsContract.View {

    @Inject
    AlarmsContract.Presenter mPresenter;

    private AlarmItemListener listener;
    private AlarmsAdapter listAdapter;
    private LinearLayout alarmsView;
    private View noAlarmsView;
    private ImageView noAlarmsIcon;
    private TextView noAlarmsMainView;
    private TextView noAlarmsAddView;

    @Inject
    public AlarmsFragment() {
    }

    @NonNull
    public static AlarmsFragment newInstance() {
        return new AlarmsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.listAdapter = new AlarmsAdapter(new ArrayList<Alarm>(0), listener);
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

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(@NonNull AlarmsContract.Presenter presenter) {
        this.mPresenter = checkNotNull(presenter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mPresenter.result(requestCode, resultCode);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.alarms_fragment, container, false);

        // Set the adapter
        ListView alarmsView = root.findViewById(R.id.alarms_list);
        alarmsView.setAdapter(this.listAdapter);

        this.alarmsView = (LinearLayout) root.findViewById(R.id.alarms_linear_layout);

        // Set up  no tasks view
        noAlarmsView = root.findViewById(R.id.no_alarms);
        noAlarmsIcon = (ImageView) root.findViewById(R.id.no_alarms_icon);
        noAlarmsMainView = (TextView) root.findViewById(R.id.no_alarms_main);
        noAlarmsAddView = (TextView) root.findViewById(R.id.no_alarms_add);
        noAlarmsAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAlarm();
            }
        });

        // Set up floating action button
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
    public void showAlarms(List<Alarm> tasks) {

    }

    @Override
    public void showAddAlarm() {
        Intent intent = new Intent(getContext(), AddEditAlarmActivity.class);
        startActivityForResult(intent, AddEditAlarmActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showNoAlarms() {
        showNoTasksViews(
                getResources().getString(R.string.no_alarms),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        alarmsView.setVisibility(View.GONE);
        noAlarmsView.setVisibility(View.VISIBLE);

        noAlarmsMainView.setText(mainText);
        noAlarmsIcon.setImageDrawable(getResources().getDrawable(iconRes));
        noAlarmsAddView.setVisibility(showAddView ? View.VISIBLE : View.GONE);
    }

    private static class AlarmsAdapter extends BaseAdapter {

        private List<Alarm> alarms;
        private final AlarmItemListener listener;

        public AlarmsAdapter(List<Alarm> alarms,
                             AlarmItemListener listener) {
            this.alarms = alarms;
            this.listener = listener;
        }


        @Override
        public int getCount() {
            return alarms.size();
        }

        @Override
        public Alarm getItem(int i) {
            return alarms.get(i);
        }

        @SuppressLint("RestrictedApi")
        private void setList(List<Alarm> tasks) {
            this.alarms = checkNotNull(tasks);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.alarm_item, viewGroup, false);
            }

            final Alarm alarm = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.item_number);
            titleTV.setText(alarm.getTitle());

            return rowView;
        }
    }

    public interface AlarmItemListener {
        void onActivateClick(Alarm activatedTask);
    }
}