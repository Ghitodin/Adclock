package com.crypt.adclock.addeditalarm;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.crypt.adclock.addeditalarm.dialogs.editlabel.EditLabelContract;
import com.crypt.adclock.addeditalarm.dialogs.editlabel.EditLabelPresenter;
import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerContract;
import com.crypt.adclock.addeditalarm.dialogs.ringtonepicker.RingtonePickerPresenter;
import com.crypt.adclock.alarms.AlarmsActivity;
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
        return activity.getIntent().getStringExtra(AlarmsActivity.EXTRA_ALARM_ID);
    }

    @Provides
    @ActivityScoped
    static boolean provideIsNeedToLoadData(AddEditAlarmActivity activity) {
        return activity.isNeedToLoadData();
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
            final AddEditAlarmContract.Presenter presenter) {
        return new RingtonePickerContract.Presenter.OnRingtoneSelectedListener() {
            @Override
            public void onRingtoneSelected(Uri uri) {
                presenter.setRingtone(uri);
            }
        };
    }

    @ActivityScoped
    @Binds
    abstract RingtonePickerContract.Presenter provideRingtonePickerPresenter(
            RingtonePickerPresenter presenter);

    @Provides
    @ActivityScoped
    static EditLabelContract.Presenter.
            OnLabelSetListener provideOnLabelSelectedListener(
            final AddEditAlarmContract.Presenter presenter) {
        return new EditLabelContract.Presenter.OnLabelSetListener() {
            @Override
            public void onLabelSet(String label) {
                presenter.setLabel(label);
            }
        };
    }

    @ActivityScoped
    @Binds
    abstract EditLabelContract.Presenter provideEditLabelPresenter(
            EditLabelPresenter presenter);
}
