package com.crypt.adclock.di;

import android.app.Application;

import com.crypt.adclock.AdClockApplication;
import com.crypt.adclock.data.source.AlarmsRepository;
import com.crypt.adclock.data.source.AlarmsRepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AlarmsRepositoryModule.class,
        AppModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<AdClockApplication> {

    AlarmsRepository getAlarmsRepository();

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }

}
