package com.example.arcius.livinghistory.data.repository;

import android.graphics.Bitmap;

import com.example.arcius.livinghistory.data.Card;

import java.util.List;

public interface DataInterface {

    interface LoadCardListener {
        void onLoading();
        void onLoaded(List<Card> cards);
        void onPicLoaded(List<Card> cards);
        void onNoConnection();
        void onNoData();
    }

    void getCards(LoadCardListener listener, String id);
    Card getCard(String date, String eventID);
    Bitmap loadImage(String imageName);

}
