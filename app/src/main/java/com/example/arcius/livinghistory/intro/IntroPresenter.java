package com.example.arcius.livinghistory.intro;

import android.app.Activity;
import android.content.SharedPreferences;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class IntroPresenter implements IntroContract.Presenter.Settings {

    private final static LocalDate startDate = new LocalDate(1939,9,1);
    private final static LocalDate endDate = new LocalDate(1945,9,2);

    private int year = 1939;

    private LocalDate myDate = new LocalDate(year,LocalDate.now().getMonthOfYear(),LocalDate.now().getDayOfMonth());
    private Interval interval = new Interval(startDate.toDateTimeAtStartOfDay(),endDate.toDateTimeAtStartOfDay());

    private IntroContract.View.Settings view;

    public IntroPresenter(IntroContract.View.Settings view){
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        setText();
        setDate();
    }

    @Override
    public void incYear() {

        if((year + 1) <= 1945){
            year++;
            myDate = new LocalDate(year,LocalDate.now().getMonthOfYear(),LocalDate.now().getDayOfMonth());
            setText();
        }
    }

    @Override
    public void decYear() {

        if((year - 1) >= 1939){
            year--;
            myDate = new LocalDate(year,LocalDate.now().getMonthOfYear(),LocalDate.now().getDayOfMonth());
            setText();
        }
    }

    private void setText() {
        if(interval.contains(myDate.toDateTimeAtStartOfDay())) { //During War
            view.showYear(Integer.toString(year));
            setDaysDuring();
        } else if (myDate.compareTo(startDate) < 0) {           //Before War
            view.showYear(Integer.toString(year));
            setDaysBefore();
        } else {                                                //After War
            view.showYear(Integer.toString(year));
            setDaysAfter();
        }
    }

    private void setDaysAfter() {
        int days = Days.daysBetween(endDate.toDateTimeAtStartOfDay(),myDate.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days after war");
    }

    private void setDaysBefore() {
        int days = Days.daysBetween(myDate.toDateTimeAtStartOfDay(),startDate.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days to initialize of war");
    }

    private void setDaysDuring() {
        int days = Days.daysBetween(myDate.toDateTimeAtStartOfDay(),endDate.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days left till end of the war.");
    }

    private void setDate() {

        String text;
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);

        if(calendar.get(Calendar.DAY_OF_MONTH) == 1){
            text = "1st of " + getMonthForInt(date.getMonth());
        } else if ((calendar.get(Calendar.DAY_OF_MONTH) == 2)) {
            text =  "2nd of " + getMonthForInt(date.getMonth());
        } else {
            text = calendar.get(Calendar.DAY_OF_MONTH) + "th of " + getMonthForInt(date.getMonth());
        }

        view.showDateText(text);
    }

    private String getMonthForInt(int num) {
        String month = "";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    @Override
    public void saveYear() {
        SharedPreferences sharedPreferences = this.view.getContext().getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("war_year", year);
        editor.putInt("start_year", new LocalDate().getYear());
        editor.apply();
    }
}
