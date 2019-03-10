package com.example.arcius.livinghistory.main;

import android.content.Context;
import android.view.animation.Animation;

import com.example.arcius.livinghistory.data.Card;

import org.joda.time.LocalDate;

import java.util.List;

public interface MainContract {

    interface View {
            void addData(Card card);
            void addData(List<Card> cards);
            void updateData(List<Card> cards);
            void showSearch();
            void showCard(String date, int eventID);
            void showDate(String date);
            void showYear(String year);
            void showDays(String days);
            void showDaysText(String text);
            void hideTodayFAB();
            void hideMessenge();
            void showNoInternetConnection();
            void showNoData();
            void showLoading();
            void hideLoading();
            void showIncDay();
            void showDecDay();
            void showToday();
            Context getContext();
    }

    interface Presenter {
        void start();
        void takeView(MainContract.View view);
        void dropView();
        void initData(MainFragment.AnimationType animationType);
        void decrementDay();
        void incrementDay();
        void loadToday();
        void refreshCards();
        LocalDate getCurrentDate();
        void setCurrentDate(LocalDate date);
        boolean isToday();
        boolean isAfter();
        boolean isBefore();
    }
}
