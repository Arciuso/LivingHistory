package com.example.arcius.livinghistory;

import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.data.repository.CardRepository;
import com.example.arcius.livinghistory.data.repository.DataInterface;
import com.example.arcius.livinghistory.data.repository.local.entity.Event;
import com.example.arcius.livinghistory.data.repository.local.entity.Location;
import com.example.arcius.livinghistory.main.MainContract;
import com.example.arcius.livinghistory.main.MainPresenter;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class MainPresenterTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private MainContract.View view;

    private MainContract.Presenter presenter;


    @Captor
    private ArgumentCaptor<DataInterface.LoadCardListener> loadCardListenerArgumentCaptor;

    private int startYear = 1939;
    private int year = 1939;

    @Before
    public void prepare() {
        MockitoAnnotations.initMocks(this);

        presenter = new MainPresenter(year, startYear, cardRepository);
        presenter.takeView(view);

        Card card = new Card(new Event(0,"19390101",null,"TestMainTitle"),new Location(0,0.0f,0.0f,"TestCountry","TestName"));
        System.out.println("BEFORE");
    }

    @Test
    public void testIsToday() {
        assertEquals(false, presenter.isToday());
    }

    @Test
    public void testIsAfter() {
        assertEquals(false, presenter.isAfter());
    }

    public void testIsBefore() {
        assertEquals(true, presenter.isBefore());
    }
}
