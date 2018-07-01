package com.example.arcius.livinghistory.main;

import com.example.arcius.livinghistory.data.Card;

import java.util.List;

public interface MainContract {

    interface View {
        void setPresenter(MainContract.Presenter presenter);
        void showCard(String eventID);
        void showCards();
        void showRefresh();
        void addData(List<Card> cards);
        void addData(Card card);
    }

    interface Presenter {
        void initData();
        void refreshCards();
    }
}
