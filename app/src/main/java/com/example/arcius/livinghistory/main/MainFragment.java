package com.example.arcius.livinghistory.main;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.example.arcius.livinghistory.di.ActivityScoped;
import com.example.arcius.livinghistory.event.EventActivity;
import com.example.arcius.livinghistory.main.Adapters.CardAdapter;
import com.example.arcius.livinghistory.search.SearchActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class MainFragment extends DaggerFragment implements MainContract.View {

    @Inject
    MainContract.Presenter presenter;

    private CardAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private Animation animationFadeOut;
    private Animation animationFadeIn;

    private LayoutAnimationController controllerFadeOut;
    private LayoutAnimationController controllerFromRight;
    private LayoutAnimationController controllerFromLeft;
    private LayoutAnimationController controllerSlideDown;

    private ImageButton incDayButton;
    private ImageButton decDayButton;

    private FloatingActionButton tofirstFab;

    private TextView date;
    private TextView daysText;
    private TextView year;
    private TextView days;

    private TextView connectionText;

    @Inject
    public MainFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.takeView(this);
        this.presenter.start();
        this.presenter.initData(); //TODO
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.presenter.dropView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_frag, container, false);

        swipeRefreshLayout = view.findViewById(R.id.SwipeRefresh);
        recyclerView = view.findViewById(R.id.RecyclerViewCards);
        progressBar = view.findViewById(R.id.ProgressBar);


        date = view.findViewById(R.id.date);
        days = view.findViewById(R.id.days);
        year = view.findViewById(R.id.yearText);
        daysText = view.findViewById(R.id.textView);

        connectionText = view.findViewById(R.id.connectionText);

        adapter = new CardAdapter(this,getContext());
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

        this.animationFadeOut = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
        this.animationFadeIn = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);

        this.controllerFadeOut = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_anim_to_fade_out);
        this.controllerFromLeft = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_anim_from_left);
        this.controllerFromRight = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_anim_from_right);
        this.controllerSlideDown = AnimationUtils.loadLayoutAnimation(getContext(),R.anim.layout_anim_slide_down);

        incDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               runFadeOutAnimation();

                new Handler().postDelayed(new Runnable() {  //TODO
                    @Override
                    public void run() {     //Instantly after animation ends
                        recyclerView.clearAnimation();
                        recyclerView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        presenter.incrementDay();

                        runFromRightAnimation();

                        if(presenter.isToday()) runHideFAB();
                        else runShowFAB();

                    }
                },animationFadeOut.getDuration() + 50);


            }
        });

        decDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFadeOutAnimation();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {     //Instantly after animation ends
                        recyclerView.clearAnimation();
                        showLoading();

                        presenter.decrementDay();

                        runFromLeftAnimation();

                        if(presenter.isToday()) runHideFAB();
                        else runShowFAB();

                    }
                },animationFadeOut.getDuration() + 50);


            }
        });

        FloatingActionButton searchFab = view.findViewById(R.id.searchFab);
        tofirstFab = view.findViewById(R.id.toFirstFab);

        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearch();
            }
        });

        tofirstFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runFadeOutAnimation();
                runHideFAB();

                new Handler().postDelayed(new Runnable() {  //TODO
                    @Override
                    public void run() {     //Instantly after animation ends
                        recyclerView.clearAnimation();
                        showLoading();

                        presenter.loadToday();

                        runSlideDownAnimation();
                    }
                },animationFadeOut.getDuration() + 50);
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
    public void addData(final List<Card> cards) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Card card : cards) {
                    adapter.add(card);
                }

                adapter.notifyDataSetChanged();
                runSlideDownAnimation();
            }
        });
    }

    @Override
    public void updateData(List<Card> cards) {
        adapter.clear();
        this.addData(cards);
    }

    @Override
    public void showSearch() {
        Intent intent = new Intent(this.getContext(), SearchActivity.class);
        intent.putExtra(SearchActivity.EXTRA_TODAY, presenter.getCurrentDate());
        startActivity(intent);
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

    @Override
    public void hideTodayFAB() {
        this.tofirstFab.setVisibility(View.GONE);
    }

    private void runFromRightAnimation() {
        hideLoading();
        recyclerView.setLayoutAnimation(controllerFromRight);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                incDayButton.setEnabled(true);
                decDayButton.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        recyclerView.scheduleLayoutAnimation();
    }

    private void runFadeOutAnimation() {
        recyclerView.setLayoutAnimation(controllerFadeOut);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                incDayButton.setEnabled(false);
                decDayButton.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        recyclerView.scheduleLayoutAnimation();
    }

    private void runSlideDownAnimation() {
        hideLoading();
        recyclerView.setLayoutAnimation(controllerSlideDown);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                incDayButton.setEnabled(true);
                decDayButton.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        recyclerView.scheduleLayoutAnimation();
    }

    private void runShowFAB() {
        if(tofirstFab.getVisibility() == View.GONE ) {  //To not run animation twice
            tofirstFab.setVisibility(View.VISIBLE);
            tofirstFab.startAnimation(animationFadeIn);
        }
    }

    private void runHideFAB() {
        animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tofirstFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        tofirstFab.startAnimation(animationFadeOut);
    }

    private void runFromLeftAnimation() {
        hideLoading();
        recyclerView.setLayoutAnimation(controllerFromLeft);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setLayoutAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                incDayButton.setEnabled(true);
                decDayButton.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void hideNoInternetConnection() {
        this.connectionText.setVisibility(View.GONE);
    }

    @Override
    public void showNoInternetConnection() {
        this.connectionText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
