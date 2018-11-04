package com.example.arcius.livinghistory.event;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class EventFragment extends DaggerFragment implements EventContract.View {

    @Inject
    EventContract.Presenter presenter;

    private TextView date;
    private TextView year;
    private TextView locationTitle;
    private TextView fullText;
    private TextView titleText;
    private TextView time;
    private TextView country;

    private ImageView imageView;
    private TextView imageTitle;
    private TextView copyRightText;
    private TextView imageSource;

    @Inject
    public EventFragment() {

    }

    @Override
    public void setPresenter(EventContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.takeView(this);
        presenter.start();
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
        country = view.findViewById(R.id.countryText);

        imageView = view.findViewById(R.id.image);
        imageTitle = view.findViewById(R.id.imageTitle);
        copyRightText = view.findViewById(R.id.textView);
        imageSource = view.findViewById(R.id.imageSource);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dropView();
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
    public void showImageTitle(String imageTitle) {
        this.imageTitle.setVisibility(View.VISIBLE);
        this.imageTitle.setText(imageTitle);
    }

    @Override
    public void showImageSource(String imageSource) {
        this.copyRightText.setVisibility(View.VISIBLE);
        this.imageSource.setVisibility(View.VISIBLE);
        this.imageSource.setText(imageSource);
    }

    @Override
    public void showLocationText(String text) {
        this.locationTitle.setText(text);
    }

    @Override
    public void showImage(Bitmap bitmap) {
        this.imageView.setVisibility(View.VISIBLE);
        this.imageView.setImageBitmap(bitmap);
    }
}
