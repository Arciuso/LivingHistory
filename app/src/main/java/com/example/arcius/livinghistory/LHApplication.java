package com.example.arcius.livinghistory;

import com.example.arcius.livinghistory.data.repository.CardRepository;
import com.example.arcius.livinghistory.dependencyInjection.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class LHApplication extends DaggerApplication {

    @Inject
    CardRepository cardRepository;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    public CardRepository getCardRepository() {
        return cardRepository;
    }
}
