package com.example.arcius.livinghistory;

import dagger.Component;

@Component(modules = MainTestModule.class)
public interface MainTestComponent {
    void inject(MainTest test);
}
