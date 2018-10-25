package com.example.arcius.livinghistory.main;


import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.data.repository.CardRepository;
import com.example.arcius.livinghistory.data.repository.DataInterface;
import com.example.arcius.livinghistory.di.ActivityScoped;

import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Named;

@ActivityScoped
public class MainPresenter implements MainContract.Presenter {

    private final CardRepository repository;

    @Nullable
    private MainContract.View view;

    private final static LocalDate startDate = new LocalDate(1939, 9, 1);
    private final static LocalDate endDate = new LocalDate(1945, 9, 2);


    private int year;       //Operating year

    private int warYear;    //Year of war to begin
    private int startYear;  //Year when the app was set

    private LocalDate myDate = new LocalDate(year, LocalDate.now().getMonthOfYear(), LocalDate.now().getDayOfMonth());
    private Interval interval = new Interval(startDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay());

    @Inject
    MainPresenter(@Named("Year") int year,@Named("StartYear") int startYear, CardRepository repository ) {
        this.year = year;
        this.warYear = year;
        this.startYear = startYear;
        this.repository = repository;
    }

    @Override
    public void start() {
        this.myDate = myDate.withYear(this.year);

        this.view.hideTodayFAB();

        setText();
        setDate();
    }

    @Override
    public void takeView(MainContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void initData() {

        repository.getCards(new DataInterface.LoadCardListener() {
            @Override
            public void onLoading() {
                view.hideMessenge();
                view.showLoading();
            }

            @Override
            public void onLoaded(List<Card> cards) {
                System.out.println("onLoaded " + getDateID());
                view.hideMessenge();
                view.hideLoading();
                view.updateData(cards);
            }

            @Override
            public void onNoConnection() {
                System.out.println("onNoConnection " + getDateID());
                view.showNoInternetConnection();
                view.hideLoading();
            }

            @Override
            public void onNoData() {
                System.out.println("onNoData " + getDateID());
                view.hideLoading();
                view.showNoData();
            }
        }, getDateID());
    }

    @Override
    public void loadToday() {

        getDateID();

        myDate = new LocalDate();

        if(this.startYear == myDate.getYear()) {    //Year of starting
            this.year = warYear;
            myDate = myDate.withYear(this.warYear);
        } else {
            int difference = myDate.getYear() - this.startYear;

            if(this.warYear + difference > 1945) {  //Over the 1945, start over again
                myDate = myDate.withYear(this.warYear);
            } else {                                //Is in range of 1939 and 1945
                this.year = this.year + difference;
                myDate = myDate.withYear(this.year);
            }
        }

        initData();

        setText();
        setDate();
    }

    @Override
    public void refreshCards() {
        initData();
    }

    @Override
    public void decrementDay() {    //TODO

        myDate = myDate.minusDays(1);

        if(isToday()) {
            loadToday();
        } else {
            setText();
            setDate();

            initData();
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

            initData();
        }
    }

    @Override
    public LocalDate getCurrentDate() {
        return this.myDate;
    }

    @Override
    public void setCurrentDate(LocalDate date) {
        this.myDate = date;
        this.year = date.getYear();
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

    private String getDateID() {
        String id = myDate.toString();
        id = id.replace("-","");
        return id;
    }
}
