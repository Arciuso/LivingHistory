package com.example.arcius.livinghistory.search;

import android.support.annotation.Nullable;

import com.example.arcius.livinghistory.di.ActivityScoped;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.util.Calendar;

import javax.inject.Inject;


@ActivityScoped
public class SearchPresenter implements SearchContract.Presenter {

    private final static LocalDate startDate = new LocalDate(1939, 9, 1);
    private final static LocalDate endDate = new LocalDate(1945, 9, 2);

    private Interval interval = new Interval(startDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay());

    @Nullable
    private SearchContract.View view;

    @Nullable
    private LocalDate today;

    @Inject
    SearchPresenter(@Nullable LocalDate date) {
        this.today = date;
    }

    @Override
    public void start() {
        setCalendar();
        setText();
    }

    @Override
    public void takeView(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public LocalDate getCurrentDate() {
        return today;
    }

    @Override
    public void setCurrentDate(LocalDate date) {
        this.today = date;
    }

    @Override
    public void dateChanged(int year, int month, int dayOfMonth) {
        today = new LocalDate(year,month,dayOfMonth);
        setText();
    }

    private void setText() {
        if (interval.contains(today.toDateTimeAtStartOfDay())) { //During War
            setDaysDuring();
        } else if (today.compareTo(startDate) < 0) {           //Before War
            setDaysBefore();
        } else {                                                //After War
            setDaysAfter();
        }
    }

    private void setDaysAfter() {
        int days = Days.daysBetween(endDate.toDateTimeAtStartOfDay(), today.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days after war");
    }

    private void setDaysBefore() {
        int days = Days.daysBetween(today.toDateTimeAtStartOfDay(), startDate.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days to start of war");
    }

    private void setDaysDuring() {
        int days = Days.daysBetween(today.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay()).getDays();
        view.showDays(Integer.toString(days));
        view.showDaysText("days left till end of the war.");
    }

    private void setCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, today.getYear());
        calendar.set(Calendar.MONTH, today.getMonthOfYear() - 1); //Don't know why ?!
        calendar.set(Calendar.DAY_OF_MONTH, today.getDayOfMonth());

        long milliTime = calendar.getTimeInMillis();

        this.view.setCalendarDate(milliTime);
    }
}
