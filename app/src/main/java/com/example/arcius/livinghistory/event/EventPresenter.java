package com.example.arcius.livinghistory.event;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.data.repository.CardRepository;
import com.example.arcius.livinghistory.dependencyInjection.ActivityScoped;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

@ActivityScoped
public class EventPresenter implements EventContract.Presenter {

    private final CardRepository repository;

    @Nullable
    private EventContract.View view;

    private String date;
    private int eventID;

    private Card card;

    @Inject
    EventPresenter( @Named("Date") String date, @Named("EventID") int eventID, CardRepository repository) {
        this.date = date;
        Log.d("EventPresenter","EventPresenter set with eventID : " + eventID);
        this.eventID = eventID;
        this.repository = repository;
        this.card = repository.getCard(eventID);
    }

    @Override
    public void initialize() {
        if(card.getType() == Card.CardTypes.Image) initPicture();
        initTexts();
    }

    @Override
    public void takeView(EventContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public LatLng getLatLng() {
        Log.d("MAP", "LAT : " + card.getLat() + " LNG : " + card.getLng());
        return new LatLng(card.getLat(), card.getLng());
    }

    private void setDate() {
        int year = Integer.parseInt(card.getDate().substring(0,4));
        int month =  Integer.parseInt(card.getDate().substring(4,6));
        int day = Integer.parseInt(card.getDate().substring(6,8));

        Log.d("DATE","ID : " + card.getDate() + " Year : " + card.getDate().substring(0,4) + " Month : " + card.getDate().substring(5,6) + " Day : " + card.getDate().substring(6,8));

        LocalDate date = new LocalDate(year,month,day);

        Calendar calendar = Calendar.getInstance();
        Date temp = date.toDate();
        calendar.setTime(temp);

        String text;

        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            text = "1st of " + getMonthForInt(temp.getMonth());
        } else if ((calendar.get(Calendar.DAY_OF_MONTH) == 2)) {
            text = "2nd of " + getMonthForInt(temp.getMonth());
        } else {
            text = calendar.get(Calendar.DAY_OF_MONTH) + "th of " + getMonthForInt(temp.getMonth());
        }

        this.view.showDate(text);
        this.view.showYear(card.getDate().substring(0,4));
    }

    private void initPicture() {
        String imageName = date + "-" + eventID;

        Bitmap image = repository.loadImage(imageName);
        this.view.showImage(image);
        this.view.showImageSource(card.getSourceImage());
        this.view.showImageTitle(card.getTitleImage());
    }

    private void initTexts() {
        this.view.showCountry(card.getCountry());
        this.view.showText(card.getFullText());
        this.view.showLocationText(card.getLocationName());
        this.view.showTitle(card.getMainTitle());
        this.view.showSource(card.getSourceName(), card.getSourceLink(), card.getSourceTitle());

        setDate();
    }

    private String getMonthForInt(int num) {
        String month = "";
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(new Locale("en"));
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11) {
            month = months[num];
        }
        return month;
    }
}
