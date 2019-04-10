package com.example.arcius.livinghistory.event;


import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;


public interface EventContract {

    interface View {
        void setPresenter(EventContract.Presenter presenter);
        void showCountry(String country);
        void showYear(String year);
        void showTime(String time);
        void showTitle(String title);
        void showDate(String date);
        void showText(String text);
        void showLocation(float latitude, float longitude);
        void showLocationText(String text);
        void showImage(Bitmap bitmap);
        void showImageTitle(String imageTitle);
        void showImageSource(String imageSource);
        void showSource(String sourceName, String sourceLink, String sourceTitle);
    }

    interface Presenter {
        void initialize();
        void takeView(EventContract.View view);
        void dropView();
        LatLng getLatLng();
    }
}
