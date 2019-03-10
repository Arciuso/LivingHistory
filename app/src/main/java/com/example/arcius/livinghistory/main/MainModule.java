package com.example.arcius.livinghistory.main;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.arcius.livinghistory.di.ActivityScoped;
import com.example.arcius.livinghistory.di.FragmentScoped;

import org.joda.time.LocalDate;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MainFragment mainFragment();

    @ActivityScoped
    @Binds abstract MainContract.Presenter mainPresenter(MainPresenter presenter);

    @Provides
    @Named("Year")
    @ActivityScoped
    static int provideYear(MainActivity activity) {
        SharedPreferences sp = activity.getApplicationContext().getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        return sp.getInt("war_year", 1939);
    }

    @Provides
    @Named("StartYear")
    @ActivityScoped
    static int provideStartYear(MainActivity activity) {
        SharedPreferences sp = activity.getApplicationContext().getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        return sp.getInt("start_year", new LocalDate().getYear());
    }

}
