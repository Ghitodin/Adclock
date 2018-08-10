package com.crypt.adclock.di;

import com.crypt.adclock.addeditalarm.AddEditAlarmActivity;
import com.crypt.adclock.addeditalarm.AddEditAlarmModule;
import com.crypt.adclock.alarms.AlarmsActivity;
import com.crypt.adclock.alarms.AlarmsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(modules = AlarmsModule.class)
    abstract AlarmsActivity alarmsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddEditAlarmModule.class)
    abstract AddEditAlarmActivity addEditAlarmActivity();
}
