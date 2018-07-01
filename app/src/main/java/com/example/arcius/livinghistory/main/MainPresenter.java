package com.example.arcius.livinghistory.main;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.data.Card;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    MainPresenter(MainContract.View view){
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void initData() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("0","08:34","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.","Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));
        cards.add(new Card("1","09:50","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.","Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("2","10:14","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.","Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("3","11:10","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.","Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem."));
        cards.add(new Card("4","12:52","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse et pretium eros.","Donec euismod nec ipsum et euismod. In diam diam, finibus vel mauris vitae, vestibulum tempus lectus. Duis dolor leo, mattis vel facilisis eu, accumsan a lorem.", R.drawable.war_pic));

        view.addData(cards);
    }

    @Override
    public void refreshCards() {    //TODO ask database for this.time events, if so call initData();

    }
}
