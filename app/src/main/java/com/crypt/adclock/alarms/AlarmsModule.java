package com.crypt.adclock.alarms;

import android.content.Context;

import com.crypt.adclock.data.Alarm;
import com.crypt.adclock.di.ActivityScoped;
import com.crypt.adclock.di.FragmentScoped;

import java.util.ArrayList;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AlarmsModule {
    @FragmentScoped
    @ContributesAndroidInjector
    abstract AlarmsFragment alarmsFragment();

    @ActivityScoped
    @Binds
    abstract AlarmsContract.Presenter alarmsPresenter(AlarmsPresenter presenter);

    @Provides
    @ActivityScoped
    static AlarmsContract.View.
            AlarmItemListener provideAlarmItemListener(
            final AlarmsContract.Presenter presenter) {
        return new AlarmsContract.View.AlarmItemListener() {
            @Override
            public void onDeactivateClick(Alarm deactivatedAlarm) {

            }

            @Override
            public void onActivateClick(Alarm activatedAlarm) {
                presenter.activateAlarm(activatedAlarm);
            }

            @Override
            public void onAlarmLongPressed(Alarm alarm) {
            }

            @Override
            public void onAlarmClicked(Alarm alarm) {
                presenter.editAlarm(alarm);
            }
        };
    }

    @Provides
    @ActivityScoped
    static AlarmsAdapter provideAlarmsAdapter(Context fragment,
                                              AlarmsContract.View.AlarmItemListener listener) {
        AlarmsAdapter alarmsAdapter = new AlarmsAdapter(
                new ArrayList<Alarm>(0),
                listener,
                fragment.getResources()
        );

        return alarmsAdapter;
    }
}
