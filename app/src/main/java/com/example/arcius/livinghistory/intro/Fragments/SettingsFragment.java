package com.example.arcius.livinghistory.intro.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.intro.IntroContract;
import com.example.arcius.livinghistory.main.MainActivity;

public class SettingsFragment extends Fragment implements IntroContract.View.Settings {

    private TextView dateText;
    private TextView daysText;
    private TextView year;
    private TextView days;

    private IntroContract.Presenter.Settings presenter;

    private Animation fadeIn;
    private Animation fadeOut;

    private View view;

    public SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public void setPresenter(IntroContract.Presenter.Settings presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_frag_settings, container, false);

        ImageButton rightArrow = view.findViewById(R.id.incButton);
        ImageButton leftArrow = view.findViewById(R.id.disButton);

        this.dateText = view.findViewById(R.id.date);
        this.daysText = view.findViewById(R.id.textView);
        this.year = view.findViewById(R.id.yearText);
        this.days = view.findViewById(R.id.daysText);

        Button saveButton = view.findViewById(R.id.saveButton);

        fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        view.setAnimation(fadeIn);

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.incYear();
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.decYear();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMain();
            }
        });

        this.view = view;

        return view;
    }

    @Override
    public void showDateText(String date) {
        this.dateText.setText(date);
    }

    @Override
    public void showYear(String year) {
        this.year.setText(year);
    }

    @Override
    public void showDays(String days) {
        this.days.setText(days);
    }

    @Override
    public void showDaysText(String text) {
        if (!text.contentEquals(this.daysText.getText())) {
            this.daysText.startAnimation(fadeOut);
            this.daysText.setText(text);
            this.daysText.startAnimation(fadeIn);
        }

    }

    @Override
    public void showMain() {
        view.startAnimation(fadeOut);
        view.setVisibility(View.INVISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        preferences.edit().putBoolean("firstrun", false).apply();
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        startActivity(intent);
    }


}
