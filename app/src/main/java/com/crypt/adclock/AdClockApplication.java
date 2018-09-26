package com.crypt.adclock;

import com.crypt.adclock.di.DaggerAppComponent;

import net.danlew.android.joda.JodaTimeAndroid;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class AdClockApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        JodaTimeAndroid.init(this);
        return DaggerAppComponent.builder().application(this).build();
    }
}
