package com.example.arcius.livinghistory.event;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;


public class EventFragment extends Fragment implements EventContract.View {

    private EventContract.Presenter presenter;

    private TextView date;
    private TextView year;
    private TextView locationTitle;
    private TextView fullText;
    private TextView titleText;
    private TextView time;
    private TextView country;

    public static EventFragment newInstance() {
        return new EventFragment();
    }

    @Override
    public void setPresenter(EventContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_frag, container, false);

        date = view.findViewById(R.id.date);
        year = view.findViewById(R.id.yearText);
        locationTitle = view.findViewById(R.id.locationTitle);
        fullText = view.findViewById(R.id.fullText);
        titleText = view.findViewById(R.id.titleText);
        time = view.findViewById(R.id.time);
        country = view.findViewById(R.id.CountryText);

        this.presenter.start();

        return view;
    }

    @Override
    public void showCountry(String country) {
        this.country.setText(country);
    }

    @Override
    public void showYear(String year) {
        this.year.setText(year);
    }

    @Override
    public void showTime(String time) {
        this.time.setText(time);
    }

    @Override
    public void showTitle(String title) {
        this.titleText.setText(title);
    }

    @Override
    public void showDate(String date) {
        this.date.setText(date);
    }

    @Override
    public void showText(String text) {
        this.fullText.setText(text);
    }

    @Override
    public void showLocation(float latitude, float longitude) {

    }

    @Override
    public void showLocationText(String text) {
        this.locationTitle.setText(text);
    }
}
