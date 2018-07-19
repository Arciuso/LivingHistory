package com.example.arcius.livinghistory.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.intro.IntroActivity;

import org.joda.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private static final String CURRENT_DATE_KEY = "CURRENT_DATE_KEY";

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mainFragment == null) {

            mainFragment = new MainFragment().newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, mainFragment);
            transaction.commit();
        }

        presenter = new MainPresenter(mainFragment);

        if(savedInstanceState != null) {
            LocalDate date = (LocalDate) savedInstanceState.getSerializable(CURRENT_DATE_KEY);
            presenter.setCurrentDate(date);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_DATE_KEY,presenter.getCurrentDate());
        System.out.println("onSaveInstanceState");
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
