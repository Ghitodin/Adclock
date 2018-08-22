package com.crypt.adclock.addeditalarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.crypt.adclock.R;
import com.crypt.adclock.util.ActivityUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class AddEditAlarmActivity extends DaggerAppCompatActivity {

    public static final String KEY_NEED_TO_LOAD_DATA = "KEY_NEED_TO_LOAD_DATA";
    public static final int REQUEST_ADD_TASK = 1;

    private boolean mIsNeedToLoadData = true; // Load data on 1-st run

    private ActionBar mActionBar;

    @Inject
    AddEditAlarmContract.Presenter mPresenter;

    @Inject
    AddEditAlarmFragment mFragment;

    @Nullable
    @Inject
    String mAlarmId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fabSaveAlarm)
    FloatingActionButton fab;

    interface OnFloatingButtonClickedListener {

        void onSaveFabClicked();

    }

    @OnClick(R.id.fabSaveAlarm)
    void onFabClicked() {
        if (mFragment != null)
            mFragment.onSaveFabClicked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_alarm_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        AddEditAlarmFragment addEditAlarmFragment = (AddEditAlarmFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        setToolbarTitle(mAlarmId);

        if (addEditAlarmFragment == null) {
            addEditAlarmFragment = mFragment;

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditAlarmFragment, R.id.contentFrame);
        }

        restoreState(savedInstanceState);
    }

    private void setToolbarTitle(@Nullable String taskId) {
        if (taskId == null) {
            mActionBar.setTitle(R.string.add_alarm);
        } else {
            mActionBar.setTitle(R.string.edit_alarm);
        }
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mIsNeedToLoadData = savedInstanceState.getBoolean(KEY_NEED_TO_LOAD_DATA);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_NEED_TO_LOAD_DATA, mPresenter.isNeedToLoadData());
        super.onSaveInstanceState(outState);
    }

    public boolean isNeedToLoadData() {
        return mIsNeedToLoadData;
    }

}
