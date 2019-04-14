package com.example.arcius.livinghistory;

import com.example.arcius.livinghistory.main.MainContract;
import com.example.arcius.livinghistory.main.MainFragment;
import com.example.arcius.livinghistory.main.MainModule;
import com.example.arcius.livinghistory.main.MainPresenter;


class MainTestModule extends MainModule {

    @Override
    public MainFragment mainFragment() {
        return new MainFragment();
    }

    @Override
    public MainContract.Presenter mainPresenter(MainPresenter presenter) {
        return presenter;
    }
}
