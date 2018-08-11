package com.crypt.adclock.alarms;

import com.crypt.adclock.di.ActivityScoped;
import com.crypt.adclock.di.FragmentScoped;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AlarmsModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract AlarmsFragment alarmsFragment();

    @ActivityScoped
    @Binds
    abstract AlarmsContract.Presenter alarmsPresenter(AlarmsPresenter presenter);
}
