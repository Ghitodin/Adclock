package com.crypt.adclock.alarms;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.crypt.adclock.R;
import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.util.WeekDays;

import java.util.List;

import javax.inject.Inject;

import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.selection.SelectionTracker;


class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.ViewHolder> {

    private List<Alarm> mAlarms;
    private AlarmsContract.View.AlarmItemListener mAlarmsItemListener;
    private Resources mResources;
    private SelectionTracker<Long> mSelectionTracker;

    @Inject
    AlarmsAdapter(@NonNull List<Alarm> alarms,
                  @NonNull AlarmsContract.View.AlarmItemListener alarmItemListener,
                  @NonNull Resources resources) {
        mAlarms = alarms;
        mResources = resources;
        mAlarmsItemListener = alarmItemListener;
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        mSelectionTracker = selectionTracker;
    }

    void replaceData(@NonNull List<Alarm> alarms) {
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
        final Alarm alarm = mAlarms.get(position);
        holder.bind(alarm, position, mSelectionTracker.isSelected((long) position));
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTimeTextView;
        private final TextView mLabelTextView;
        private final TextView mRepeatDaysTextView;
        private final Switch mIsActiveSwitch;
        private final View mMainView;

        private ItemDetails mDetails;

        ViewHolder(View v) {
            super(v);
            mMainView = v;
            mTimeTextView = v.findViewById(R.id.alarm_time_tw);
            mLabelTextView = v.findViewById(R.id.alarm_label_tw);
            mRepeatDaysTextView = v.findViewById(R.id.alarm_repeat_tw);
            mIsActiveSwitch = v.findViewById(R.id.is_alarm_active_switch);
            mDetails = new ItemDetails();
        }

        void bind(@NonNull final Alarm alarm, int pos, boolean isSelected) {
            mDetails.mPos = pos;
            itemView.setActivated(isSelected);

            String alarmHours = alarm.getTime().getHourOfDay() < 10 ?
                    ("0" + String.valueOf(alarm.getTime().getHourOfDay())) :
                    String.valueOf(alarm.getTime().getHourOfDay());
            String alarmMinutes = alarm.getTime().getMinuteOfHour() < 10 ?
                    ("0" + String.valueOf(alarm.getTime().getMinuteOfHour())) :
                    String.valueOf(alarm.getTime().getMinuteOfHour());
            String time = alarmHours + ":" + alarmMinutes;
            mTimeTextView.setText(time);
            mLabelTextView.setText(alarm.getTitle());
            mIsActiveSwitch.setChecked(alarm.isActive());

            List<String> daysNames = WeekDays.getAllDaysShortNames();

            List<Boolean> repeatDays = alarm.getRepeatDays();
            StringBuilder repeatLabel = new StringBuilder();
            for (int i = 0; i < repeatDays.size(); ++i) {
                if (repeatDays.get(i)) {
                    repeatLabel.append(daysNames.get(i)).append(" ");
                }
            }
            String repeatLabelString = repeatLabel.toString();
            if (repeatLabelString.isEmpty()) {
                repeatLabelString = mResources.getString(R.string.one_time_alarm_label);
            }

            mRepeatDaysTextView.setText(repeatLabelString);

            mMainView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mAlarmsItemListener.onAlarmLongPressed(alarm);
                    return true;
                }
            });

            mMainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlarmsItemListener.onAlarmClicked(alarm);
                }
            });
        }

        ItemDetails getItemDetails() {
            return mDetails;
        }
    }

    static class ItemDetails extends ItemDetailsLookup.ItemDetails<Long> {
        long mPos = -1;

        ItemDetails() {
        }

        @Override
        public int getPosition() {
            return (int) mPos;
        }

        @Nullable
        @Override
        public Long getSelectionKey() {
            return mPos;
        }
    }

    static class KeyProvider extends ItemKeyProvider<Long> {

        KeyProvider() {
            super(ItemKeyProvider.SCOPE_MAPPED);
        }

        @Nullable
        @Override
        public Long getKey(int i) {
            return (long) i;
        }

        @Override
        public int getPosition(@NonNull Long key) {
            return key.intValue();
        }
    }

    static class DetailsLookup extends ItemDetailsLookup<Long> {

        private RecyclerView mRecyclerView;

        DetailsLookup(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
        }

        @Nullable
        @Override
        public ItemDetails<Long> getItemDetails(@NonNull MotionEvent motionEvent) {
            final View view = mRecyclerView.findChildViewUnder(motionEvent.getX(),
                    motionEvent.getY());
            if (view != null) {
                RecyclerView.ViewHolder viewHolder = mRecyclerView.getChildViewHolder(view);
                if (viewHolder instanceof ViewHolder) {
                    return ((ViewHolder) viewHolder).getItemDetails();
                }
            }
            return null;
        }
    }

    static class Predicate extends SelectionTracker.SelectionPredicate<Long> {

        @Override
        public boolean canSetStateForKey(@NonNull Long key, boolean nextState) {
            return true;
        }

        @Override
        public boolean canSetStateAtPosition(int position, boolean nextState) {
            return true;
        }

        @Override
        public boolean canSelectMultiple() {
            return true;
        }
    }
}