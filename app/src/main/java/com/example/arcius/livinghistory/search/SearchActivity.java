package com.example.arcius.livinghistory.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.example.arcius.livinghistory.R;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SearchActivity extends DaggerAppCompatActivity {

    public static final String EXTRA_TODAY = "TODAY";
    public static final String CURRENT_DATE_KEY = "CURRENT_DATE_KEY";

    @Inject
    SearchContract.Presenter presenter;

    @Inject
    SearchFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_act);

        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (searchFragment == null) {
            searchFragment = fragment;

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, searchFragment);
            transaction.commit();
        }

        if(savedInstanceState != null) {
            LocalDate date = (LocalDate) savedInstanceState.getSerializable(CURRENT_DATE_KEY);
            presenter.setCurrentDate(date);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_DATE_KEY,presenter.getCurrentDate());
        super.onSaveInstanceState(outState);
    }
}
