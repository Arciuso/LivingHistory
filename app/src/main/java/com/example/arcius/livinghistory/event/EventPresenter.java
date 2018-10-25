package com.example.arcius.livinghistory.event;

import com.example.arcius.livinghistory.data.Card;

import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EventPresenter implements EventContract.Presenter {

    private EventContract.View view;

    private Card card;

    EventPresenter(EventContract.View view, Card card) {
        this.view = view;
        this.view.setPresenter(this);
        this.card = card;
    }

    @Override
    public void start() {
        this.view.showCountry(card.getLocation().getCountry());
        this.view.showText(card.getFullText());
        this.view.showLocationText(card.getLocation().getName());
        this.view.showTitle(card.getMainTitle());
        setDate();
    }

    private void setDate() {
        int year = Integer.parseInt(card.getDate().substring(0,4));
        int month =  Integer.parseInt(card.getDate().substring(5,6));
        int day = Integer.parseInt(card.getDate().substring(7,8));

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
