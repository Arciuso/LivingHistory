package com.example.arcius.livinghistory.search;

import com.example.arcius.livinghistory.di.ActivityScoped;
import com.example.arcius.livinghistory.di.FragmentScoped;


import org.joda.time.LocalDate;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

import static com.example.arcius.livinghistory.search.SearchActivity.EXTRA_TODAY;

@Module
public abstract class SearchModule {

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchFragment searchFragment();

    @ActivityScoped
    @Binds abstract SearchContract.Presenter searchPresenter(SearchPresenter presenter);

    @Provides
    @ActivityScoped
    static LocalDate provideCurrentDate(SearchActivity activity) {
        return (LocalDate) activity.getIntent().getSerializableExtra(EXTRA_TODAY);
    }
}
