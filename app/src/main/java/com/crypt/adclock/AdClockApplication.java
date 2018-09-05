package com.crypt.adclock;

import com.crypt.adclock.data.source.AlarmsRepository;
import com.crypt.adclock.di.DaggerAppComponent;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class AdClockApplication extends DaggerApplication {
    @Inject
    AlarmsRepository mRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        JodaTimeAndroid.init(this);
        return DaggerAppComponent.builder().application(this).build();
    }

    public AlarmsRepository getRepository() {
        return mRepository;
    }
}
