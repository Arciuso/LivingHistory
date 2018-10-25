package com.example.arcius.livinghistory.data.repository;

import com.example.arcius.livinghistory.data.Card;

import java.util.List;

public interface DataInterface {

    interface LoadCardListener {
        void onLoading();
        void onLoaded(List<Card> cards);
        void onNoConnection();
        void onNoData();
    }

    /*
    interface LoadImageListener {
        void onLoaded(Card card);
        void onNoConnection();
    }
    */

    void getCards(LoadCardListener listener, String id);
}
