package com.example.arcius.livinghistory.event;

import com.example.arcius.livinghistory.di.ActivityScoped;
import com.example.arcius.livinghistory.di.FragmentScoped;


import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class EventModule {

    @Provides
    @Named("Date")
    @ActivityScoped
    static String provideDate(EventActivity activity) {
        return activity.getIntent().getStringExtra(EventActivity.EXTRA_EVENT_DATE);
    }

    @Provides
    @Named("EventID")
    @ActivityScoped
    static int provideEventID(EventActivity activity) {
        return activity.getIntent().getIntExtra(EventActivity.EXTRA_EVENT_ID, 0);
    }

    @FragmentScoped
    @ContributesAndroidInjector
    abstract EventFragment eventFragment();

    @ActivityScoped
    @Binds
    abstract EventContract.Presenter eventPresenter(EventPresenter presenter);

}
