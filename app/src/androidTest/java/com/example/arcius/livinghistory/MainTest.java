package com.example.arcius.livinghistory;

import android.support.test.runner.AndroidJUnit4;


import com.example.arcius.livinghistory.main.MainContract;
import com.example.arcius.livinghistory.main.MainFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(AndroidJUnit4.class)
public class MainTest {

    @Inject
    MainFragment mainFragment;

    @Inject
    MainContract.Presenter mainPresenter;

    @Before
    public void prepare() {
        MainTestComponent component = DaggerMainTestComponent.builder().MainModule(new MainTestModule()).build();
        component.inject(this);
    }

    @Test
    public void testPresenter() {
        mainFragment.
    }

}
