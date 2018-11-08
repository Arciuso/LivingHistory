package com.example.arcius.livinghistory.event;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class EventFragment extends DaggerFragment implements EventContract.View, OnMapReadyCallback {

    public static final String MAP_VIEW_KEY = "MAP_KEY";

    @Inject
    EventContract.Presenter presenter;

    private TextView date;
    private TextView year;
    private TextView locationTitle;
    private TextView fullText;
    private TextView titleText;
    private TextView time;
    private TextView country;

    private TextView sourceName;
    private TextView sourceTitle;

    private ImageView imageView;
    private TextView imageTitle;
    private TextView copyRightText;
    private TextView imageSource;

    private GoogleMap googleMap;
    private MapView mapView;

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
        mapView.onResume();
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

        sourceName = view.findViewById(R.id.sourceName);
        sourceTitle = view.findViewById(R.id.sourceTitle);

        imageView = view.findViewById(R.id.image);
        imageTitle = view.findViewById(R.id.imageTitle);
        copyRightText = view.findViewById(R.id.textView);
        imageSource = view.findViewById(R.id.imageSource);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_KEY);
        }

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        presenter.dropView();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("MAP READY");
        this.googleMap = googleMap;
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(presenter.getLatLng()));
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
    public void showSource(String sourceName, String sourceLink, String sourceTitle) {
        String text = sourceName + " :";
        String link = "<a href='" + sourceLink + "'>" + sourceTitle + "</a>";

        Spanned html = Html.fromHtml(link);
        this.sourceTitle.setMovementMethod(LinkMovementMethod.getInstance());
        this.sourceTitle.setText(html);
        this.sourceName.setText(text);
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
