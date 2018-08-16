package com.example.arcius.livinghistory.search;

import org.joda.time.LocalDate;

public interface SearchContract {

    interface View {
        void setPresenter(SearchContract.Presenter presenter);
        void setCalendarDate(long milisec);
    }

    interface Presenter {
        void start();
        LocalDate getCurrentDate();
        void setCurrentDate(LocalDate date);
    }
}
