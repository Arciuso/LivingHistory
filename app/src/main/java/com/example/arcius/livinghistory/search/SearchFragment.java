package com.example.arcius.livinghistory.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.main.MainActivity;


public class SearchFragment extends Fragment implements SearchContract.View {

    private SearchContract.Presenter presenter;

    private CalendarView calendarView;

    private TextView daysText;
    private TextView days;

    private Button button;

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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                presenter.dateChanged(year,month + 1,dayOfMonth);
            }
        });

        button = view.findViewById(R.id.searchButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDay();
            }
        });

        daysText = view.findViewById(R.id.textView);
        days = view.findViewById(R.id.days);

        return view;
    }

    @Override
    public void setCalendarDate(long milisec) {
        this.calendarView.setDate (milisec, true, true);
    }

    @Override
    public void showDays(String days) {
        this.days.setText(days);
    }

    @Override
    public void showDaysText(String text) {
        this.daysText.setText(text);
    }

    @Override
    public void showDay() {
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_DATE_KEY, presenter.getCurrentDate());
        startActivity(intent);
    }
}
