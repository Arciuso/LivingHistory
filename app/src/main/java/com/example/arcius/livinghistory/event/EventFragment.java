package com.example.arcius.livinghistory.event;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.arcius.livinghistory.R;

public class EventFragment extends Fragment implements EventContract.View {

    private EventContract.Presenter presenter;

    public static EventFragment newInstance() {
        return new EventFragment();
    }

    @Override
    public void setPresenter(EventContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_frag, container, false);

        return view;
    }

    @Override
    public void showCountry(String country) {

    }

    @Override
    public void showYear(String year) {

    }

    @Override
    public void showTime(String time) {

    }

    @Override
    public void showTitle(String title) {

    }

    @Override
    public void showText(String text) {

    }

    @Override
    public void showLocation() {

    }

    @Override
    public void showLocationText(String text) {

    }
}
