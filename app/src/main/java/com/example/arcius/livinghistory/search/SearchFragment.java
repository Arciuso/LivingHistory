package com.example.arcius.livinghistory.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.dependencyInjection.ActivityScoped;
import com.example.arcius.livinghistory.main.MainActivity;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SearchFragment extends DaggerFragment implements SearchContract.View {

    @Inject
    SearchContract.Presenter presenter;

    private CalendarView calendarView;

    private TextView daysText;
    private TextView days;

    @Inject
    public SearchFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.takeView(this);
        this.presenter.initialize();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.dropView();
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

        Button button = view.findViewById(R.id.searchButton);

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
        Log.d("CURRENT_DAY", "CURRENT_DAY : " + presenter.getCurrentDate());
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_DATE_KEY, presenter.getCurrentDate());
        startActivity(intent);
    }
}
