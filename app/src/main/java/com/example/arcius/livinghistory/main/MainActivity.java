package com.example.arcius.livinghistory.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.intro.IntroActivity;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    public static final String CURRENT_DATE_KEY = "CURRENT_DATE_KEY";

    @Inject
    MainContract.Presenter presenter;

    @Inject
    MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);


        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mainFragment == null) {
            mainFragment = fragment;

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, mainFragment);
            transaction.commit();

        }

        LocalDate day = (LocalDate) getIntent().getSerializableExtra(CURRENT_DATE_KEY); //From search
        if(day != null)
            presenter.setCurrentDate(day);

        if(savedInstanceState != null) {
            LocalDate date = (LocalDate) savedInstanceState.getSerializable(CURRENT_DATE_KEY);  //After rotation
            presenter.setCurrentDate(date);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_DATE_KEY,presenter.getCurrentDate());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if (preferences.getBoolean("firstrun", true)) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
    }
}
