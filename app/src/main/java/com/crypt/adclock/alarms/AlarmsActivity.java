package com.crypt.adclock.alarms;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.crypt.adclock.R;
import com.crypt.adclock.util.ActivityUtils;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class AlarmsActivity extends DaggerAppCompatActivity {
    public static final String EXTRA_ALARM_ID = "ALARM_ID";

    @Inject
    AlarmsContract.Presenter mAlarmsPresenter;
    @Inject
    Lazy<AlarmsFragment> mAlarmsFragmentProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarms_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AlarmsFragment tasksFragment =
                (AlarmsFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame_alarms);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = mAlarmsFragmentProvider.get();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.content_frame_alarms);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alarms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
