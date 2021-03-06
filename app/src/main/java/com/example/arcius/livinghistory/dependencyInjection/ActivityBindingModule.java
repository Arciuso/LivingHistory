package com.example.arcius.livinghistory.dependencyInjection;

import com.example.arcius.livinghistory.event.EventActivity;
import com.example.arcius.livinghistory.event.EventModule;
import com.example.arcius.livinghistory.main.MainActivity;
import com.example.arcius.livinghistory.main.MainModule;
import com.example.arcius.livinghistory.search.SearchActivity;
import com.example.arcius.livinghistory.search.SearchModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SearchModule.class)
    abstract SearchActivity searchActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = EventModule.class)
    abstract EventActivity eventActivity();

}
