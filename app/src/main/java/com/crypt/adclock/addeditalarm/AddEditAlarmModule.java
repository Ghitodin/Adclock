package com.crypt.adclock.addeditalarm;

import android.support.annotation.Nullable;

import com.crypt.adclock.di.ActivityScoped;
import com.crypt.adclock.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AddEditAlarmModule {
    @Provides
    @ActivityScoped
    @Nullable
    static String provideAlarmId(AddEditAlarmActivity activity) {
        return activity.getIntent().getStringExtra(AddEditAlarmFragment.ARGUMENT_EDIT_TASK_ID);
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddEditAlarmFragment addEditAlarmFragment();

    @ActivityScoped
    @Binds
    abstract AddEditAlarmContract.Presenter addEditAlarmPresenter(AddEditAlarmPresenter presenter);
}
