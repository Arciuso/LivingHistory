package com.example.arcius.livinghistory.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.arcius.livinghistory.R;


public class SearchFragment extends Fragment implements SearchContract.View {

    private SearchContract.Presenter presenter;

    private CalendarView calendarView;

    public SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_frag, container, false);

        calendarView = view.findViewById(R.id.calendarView);

        return view;
    }

    @Override
    public void setCalendarDate(long milisec) {
        calendarView.setDate (milisec, true, true);
    }
}
