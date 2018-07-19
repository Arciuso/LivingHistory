package com.example.arcius.livinghistory.main;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.data.Card;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    private final static LocalDate startDate = new LocalDate(1939, 9, 1);
    private final static LocalDate endDate = new LocalDate(1945, 9, 2);

    private int year = 1939;

    private LocalDate myDate = new LocalDate(year, LocalDate.now().getMonthOfYear(), LocalDate.now().getDayOfMonth());
    private Interval interval = new Interval(startDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay());

    MainPresenter(MainContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        SharedPreferences sp = this.view.getContext().getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        this.year = sp.getInt("year", 1939);
        this.myDate = myDate.withYear(this.year);



        setText();
        setDate();
    }

    @Override
    public void initData() {    //TODO

        List<Card> cards = new ArrayList<>();
        cards.add(new Card("0", "08:34", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));
        cards.add(new Card("1", "09:50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("2", "10:14", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("3", "11:10", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("4", "12:52", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));

        view.addData(cards);
    }

    @Override
    public void loadToday() {
        myDate = new LocalDate();
        myDate = myDate.withYear(this.year);

        //TODO load today
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("0", "08:34", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));
        cards.add(new Card("1", "09:50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("2", "10:14", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("3", "11:10", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("4", "12:52", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));

        view.updateData(cards);

        setText();
        setDate();
    }

    @Override
    public void refreshCards() {    //TODO ask database for this.time events, if so call initData();
        System.out.println("Refresh cards !");
    }

    @Override
    public void decrementDay() {    //TODO

        myDate = myDate.minusDays(1);

        if(isToday()) {
            loadToday();
        } else {
            setText();
            setDate();

            List<Card> cards = new ArrayList<>();
            cards.add(new Card("0", "08:34", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));
            cards.add(new Card("1", "09:50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
            cards.add(new Card("4", "12:52", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));

            view.updateData(cards);
        }
    }

    @Override
    public void incrementDay() {    //TODO

        myDate = myDate.plusDays(1);

        if(isToday()) {
            loadToday();
        } else {
            setText();
            setDate();

            List<Card> cards = new ArrayList<>();
            cards.add(new Card("0", "08:34", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));
            cards.add(new Card("1", "09:50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
            cards.add(new Card("1", "09:50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
            cards.add(new Card("1", "09:50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
            cards.add(new Card("1", "09:50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
            cards.add(new Card("1", "09:50", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.", "Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));

            view.updateData(cards);
        }
    }

    @Override
    public LocalDate getCurrentDate() {
        return this.myDate;
    }

    @Override
    public void setCurrentDate(LocalDate date) {
        this.myDate = date;
    }

    @Override
    public boolean isToday() {
        return myDate.equals(new LocalDate().withYear(this.year));
    }

    private void setText() {
        if (interval.contains(myDate.toDateTimeAtStartOfDay())) { //During War
            view.showYear(Integer.toString(year));
            setDaysDuring();
        } else if (myDate.compareTo(startDate) < 0) {           //Before War
            view.showYear(Integer.toString(year));
            setDaysBefore();
        } else {                                                //After War
            view.showYear(Integer.toString(year));
            setDaysAfter();
        }

        System.out.println(myDate.toString());
    }

    private void setDaysAfter() {
        int days = Days.daysBetween(endDate.toDateTimeAtStartOfDay(), myDate.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days after war");
    }

    private void setDaysBefore() {
        int days = Days.daysBetween(myDate.toDateTimeAtStartOfDay(), startDate.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days to start of war");
    }

    private void setDaysDuring() {
        int days = Days.daysBetween(myDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days left till end of the war.");
    }

    private void setDate() {

        String text;
        Calendar calendar = Calendar.getInstance();
        Date date = myDate.toDate();
        calendar.setTime(date);

        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            text = "1st of " + getMonthForInt(date.getMonth());
        } else if ((calendar.get(Calendar.DAY_OF_MONTH) == 2)) {
            text = "2nd of " + getMonthForInt(date.getMonth());
        } else {
            text = calendar.get(Calendar.DAY_OF_MONTH) + "th of " + getMonthForInt(date.getMonth());
        }

        view.showDate(text);
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
