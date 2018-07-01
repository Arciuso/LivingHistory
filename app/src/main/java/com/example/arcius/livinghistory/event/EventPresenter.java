package com.example.arcius.livinghistory.event;

import com.example.arcius.livinghistory.data.Card;

public class EventPresenter implements EventContract.Presenter {

    private EventContract.View view;

    private String eventID;

    EventPresenter(EventContract.View view, String eventID) {
        this.view = view;
        this.view.setPresenter(this);
        this.eventID = eventID;
    }

    @Override
    public void start() {

    }

    private void showEvent(Card card) {

    }
}
