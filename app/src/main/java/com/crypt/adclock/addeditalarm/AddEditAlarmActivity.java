package com.crypt.adclock.addeditalarm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crypt.adclock.R;

public class AddEditAlarmActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_alarm_activity);
    }
}
