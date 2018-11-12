package com.example.arcius.livinghistory.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SymbolTable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.intro.IntroActivity;
import com.google.android.gms.security.ProviderInstaller;

import org.joda.time.LocalDate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements ProviderInstaller.ProviderInstallListener{

    public static final String CURRENT_DATE_KEY = "CURRENT_DATE_KEY";

    @Inject
    MainContract.Presenter presenter;

    @Inject
    MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);

        ProviderInstaller.installIfNeededAsync(this,this);

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
            saveYear();
            preferences.edit().putBoolean("firstrun", false).apply();
        }
    }

    @Override
    public void onProviderInstalled() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            assert sslContext != null;
            sslContext.init(null, null, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLEngine engine = sslContext.createSSLEngine();
        engine.getEnabledProtocols();
    }

    @Override
    public void onProviderInstallFailed(int i, Intent intent) {

    }

    private void saveYear() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("war_year", 1939);
        editor.putInt("start_year", new LocalDate().getYear());
        editor.apply();
    }
}

