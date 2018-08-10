package com.crypt.adclock.addeditalarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.crypt.adclock.R;
import com.crypt.adclock.util.ActivityUtils;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class AddEditAlarmActivity extends DaggerAppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;
    private ActionBar actionBar;

    @Inject
    AddEditAlarmContract.Presenter mAddEditPresenter;

    @Inject
    AddEditAlarmFragment mFragment;

    @Nullable
    @Inject
    String mAlarmId;

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

        AddEditAlarmFragment addEditAlarmFragment = (AddEditAlarmFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        setToolbarTitle(mAlarmId);

        if (addEditAlarmFragment == null) {
            addEditAlarmFragment = mFragment;

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditAlarmFragment, R.id.contentFrame);
        }
    }

    private void setToolbarTitle(@Nullable String taskId) {
        if (taskId == null) {
            actionBar.setTitle(R.string.add_alarm);
        } else {
            actionBar.setTitle(R.string.edit_alarm);
        }
    }
}
