package com.crypt.adclock.addeditalarm;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crypt.adclock.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddEditAlarmFragment extends Fragment {

    public AddEditAlarmFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}
