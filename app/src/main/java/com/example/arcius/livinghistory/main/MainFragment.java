package com.example.arcius.livinghistory.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.data.Card;
import com.example.arcius.livinghistory.event.EventActivity;
import com.example.arcius.livinghistory.main.Adapters.CardAdapter;

import java.util.List;

public class MainFragment extends Fragment implements MainContract.View {

    MainContract.Presenter presenter;
    CardAdapter adapter;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    LayoutAnimationController controllerToRight;
    LayoutAnimationController controllerToLeft;
    LayoutAnimationController controllerFromRight;
    LayoutAnimationController controllerFromLeft;

    ImageButton incDayButton;
    ImageButton decDayButton;

    private TextView date;
    private TextView daysText;
    private TextView year;
    private TextView days;

    public MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_frag, container, false);

        swipeRefreshLayout = view.findViewById(R.id.SwipeRefresh);
        recyclerView = view.findViewById(R.id.RecyclerViewCards);
        progressBar = view.findViewById(R.id.ProgressBar);

        date = view.findViewById(R.id.date);
        days = view.findViewById(R.id.days);
        year = view.findViewById(R.id.yearText);
        daysText = view.findViewById(R.id.textView);

        adapter = new CardAdapter(this,getContext());
        recyclerView.setAdapter(adapter);

        presenter.initData();

        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshCards();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        incDayButton = view.findViewById(R.id.incButton);
        decDayButton = view.findViewById(R.id.decButton);

        this.controllerToRight = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),R.anim.layout_anim_to_right);
        this.controllerToLeft = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),R.anim.layout_anim_to_left);
        this.controllerFromRight = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),R.anim.layout_anim_from_right);
        this.controllerFromLeft = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),R.anim.layout_anim_from_left);

        incDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runToRightAnimation();
                presenter.incrementDay();
            }
        });

        decDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runToLeftAnimation();
                presenter.decrementDay();
            }
        });

        FloatingActionButton refreshFab = view.findViewById(R.id.refreshFab);
        FloatingActionButton tofirstFab = view.findViewById(R.id.toFirstFab);

        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.refreshCards();
            }
        });

        tofirstFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadToday();
            }
        });

        return view;
    }

    @Override
    public void addData(Card card) {
        adapter.add(card);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void addData(List<Card> cards) {
        for (Card card : cards) {
            adapter.add(card);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void updateData(List<Card> cards) {
        adapter.clear();
        this.addData(cards);
    }

    @Override
    public void showCard(String eventID) {
        Intent intent = new Intent(this.getContext(), EventActivity.class);
        intent.putExtra(EventActivity.EXTRA_EVENT_ID, eventID);
        startActivity(intent);
    }

    @Override
    public void showDate(String date) {
        this.date.setText(date);
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
        this.daysText.setText(text);
    }

    private void runToRightAnimation() {

        this.controllerToRight.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                incDayButton.setEnabled(false);
                decDayButton.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                recyclerView.clearAnimation();
                recyclerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                System.out.println("ON END TO RIGHT !");

                runFromRightAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        recyclerView.setLayoutAnimation(controllerToRight);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void runToLeftAnimation() {

        this.controllerToLeft.getAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                incDayButton.setEnabled(false);
                decDayButton.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                recyclerView.clearAnimation();
                recyclerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                runFromLeftAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        recyclerView.setLayoutAnimation(controllerToLeft);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    private void runFromRightAnimation() {
        recyclerView.setLayoutAnimation(controllerFromRight);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                recyclerView.clearAnimation();
                incDayButton.setEnabled(true);
                decDayButton.setEnabled(true);
                System.out.println("ON END FROM RIGHT !");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.startLayoutAnimation();
    }

    private void runFromLeftAnimation() {
        recyclerView.setLayoutAnimation(controllerFromLeft);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                recyclerView.clearAnimation();
                incDayButton.setEnabled(true);
                decDayButton.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.scheduleLayoutAnimation();
    }
}
