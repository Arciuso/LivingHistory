package com.example.arcius.livinghistory.event;


public interface EventContract {

    interface View {
        void setPresenter(EventContract.Presenter presenter);
        void showCountry(String country);
        void showYear(String year);
        void showTime(String time);
        void showTitle(String title);
        void showText(String text);
        void showLocation(); //TODO axis
        void showLocationText(String text);
    }

    interface Presenter {
        void start();
    }
}
