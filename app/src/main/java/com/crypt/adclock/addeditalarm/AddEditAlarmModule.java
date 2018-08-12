package com.crypt.adclock.addeditalarm;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerContract;
import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerPresenter;
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
        return activity.getIntent().getStringExtra(AddEditAlarmFragment.ARGUMENT_EDIT_ALARM_ID);
    }

    @Provides
    @ActivityScoped
    static FragmentManager provideFragmentManager(AddEditAlarmActivity activity) {
        return activity.getSupportFragmentManager();
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddEditAlarmFragment addEditAlarmFragment();

    @ActivityScoped
    @Binds
    abstract AddEditAlarmContract.Presenter provideAddEditAlarmPresenter(
            AddEditAlarmPresenter presenter);

    @Provides
    @ActivityScoped
    static RingtonePickerContract.Presenter.
            OnRingtoneSelectedListener provideOnRingtoneSelectedListener(
                    final AddEditAlarmPresenter presenter) {
        return new RingtonePickerContract.Presenter.OnRingtoneSelectedListener() {
            @Override
            public void onRingtoneSelected(Uri ringtoneUri) {
                presenter.setRingtone("kek");
            }
        };
    }

    @ActivityScoped
    @Binds
    abstract RingtonePickerContract.Presenter provideRingtonePickerPresenter(
            RingtonePickerPresenter presenter);
}
