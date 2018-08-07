package com.crypt.adclock.addeditalarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.crypt.adclock.R;
import com.crypt.adclock.util.ActivityUtils;

public class AddEditAlarmActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;
    private ActionBar actionBar;

    private AddEditAlarmPresenter mAddEditPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_alarm_activity);

        // Set up the toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        AddEditAlarmFragment addEditTaskFragment = (AddEditAlarmFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        String alarmId = getIntent().getStringExtra(AddEditAlarmFragment.ARGUMENT_EDIT_ALARM_ID);

        setToolbarTitle(alarmId);

        if (addEditTaskFragment == null) {
            addEditTaskFragment = AddEditAlarmFragment.newInstance();

            if (getIntent().hasExtra(AddEditAlarmFragment.ARGUMENT_EDIT_ALARM_ID)) {
                Bundle bundle = new Bundle();
                bundle.putString(AddEditAlarmFragment.ARGUMENT_EDIT_ALARM_ID, alarmId);
                addEditTaskFragment.setArguments(bundle);
            }

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditTaskFragment, R.id.contentFrame);
        }

        mAddEditPresenter = new AddEditAlarmPresenter(
                this,
                addEditTaskFragment,
                alarmId
        );
    }

    private void setToolbarTitle(@Nullable String taskId) {
        if (taskId == null) {
            actionBar.setTitle(R.string.add_alarm);
        } else {
            actionBar.setTitle(R.string.edit_alarm);
        }
    }
}
