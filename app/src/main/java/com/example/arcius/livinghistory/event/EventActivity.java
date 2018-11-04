package com.example.arcius.livinghistory.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.example.arcius.livinghistory.R;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class EventActivity extends DaggerAppCompatActivity {

    public static final String EXTRA_EVENT_ID = "EVENT_ID";
    public static final String EXTRA_EVENT_DATE = "EVENT_DATE";

    @Inject
    EventFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_act);

        loadEventFragment();
    }

    private void loadEventFragment() {

        EventFragment eventFragment = (EventFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (eventFragment == null) {

            eventFragment = fragment;

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, eventFragment);
            transaction.commit();
        }
    }


}
