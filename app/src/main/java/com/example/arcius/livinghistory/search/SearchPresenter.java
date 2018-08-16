package com.example.arcius.livinghistory.search;

import org.joda.time.LocalDate;

import java.util.Calendar;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View view;

    private String today;

    SearchPresenter( SearchContract.View view, String today) {
        this.view = view;
        this.view.setPresenter(this);
        this.today = today;
        System.out.println(today);
    }

    @Override
    public void start() {
        setCalendar();
    }

    @Override
    public LocalDate getCurrentDate() {
        return null;
    }

    @Override
    public void setCurrentDate(LocalDate date) {

    }

    private void setCalendar() {
        String parts[] = today.split("/");

        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); //Don't know why ?!
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milliTime = calendar.getTimeInMillis();

        this.view.setCalendarDate(milliTime);
    }
}
