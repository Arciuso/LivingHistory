package com.example.arcius.livinghistory.event;

import com.example.arcius.livinghistory.di.ActivityScoped;
import com.example.arcius.livinghistory.di.FragmentScoped;


import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class EventModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract EventFragment eventFragment();

    @ActivityScoped
    @Binds
    abstract EventContract.Presenter eventPresenter(EventPresenter presenter);

}
