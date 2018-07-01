package com.example.arcius.livinghistory.event;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.arcius.livinghistory.R;

public class EventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT_ID = "EVENT_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_act);

        loadEventFragment();
    }

    private void loadEventFragment() {

        EventFragment eventFragment = (EventFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (eventFragment == null) {

            eventFragment = new EventFragment().newInstance();

            String eventID = getIntent().getStringExtra(EXTRA_EVENT_ID);

            new EventPresenter(eventFragment, eventID);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, eventFragment);
            transaction.commit();
        }
    }
}
