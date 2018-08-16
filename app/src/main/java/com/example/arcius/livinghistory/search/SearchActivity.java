package com.example.arcius.livinghistory.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.arcius.livinghistory.R;

import org.joda.time.LocalDate;

public class SearchActivity extends AppCompatActivity {

    public static final String EXTRA_TODAY = "TODAY";
    public static final String CURRENT_DATE_KEY = "CURRENT_DATE_KEY";

    private SearchContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_act);

        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (searchFragment == null) {

            searchFragment = new SearchFragment().newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, searchFragment);
            transaction.commit();
        }

        if(savedInstanceState != null) {
            LocalDate date = (LocalDate) savedInstanceState.getSerializable(CURRENT_DATE_KEY);
            presenter.setCurrentDate(date);
        } else {    //First started
            String today = getIntent().getStringExtra(EXTRA_TODAY);
            presenter = new SearchPresenter(searchFragment, today);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_DATE_KEY,presenter.getCurrentDate());
        super.onSaveInstanceState(outState);
    }
}
