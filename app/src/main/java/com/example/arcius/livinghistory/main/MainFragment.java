package com.example.arcius.livinghistory.main;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.arcius.livinghistory.dependencyInjection.ActivityScoped;
import com.example.arcius.livinghistory.event.EventActivity;
import com.example.arcius.livinghistory.main.Adapters.CardAdapter;
import com.example.arcius.livinghistory.search.SearchActivity;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class MainFragment extends DaggerFragment implements MainContract.View{

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
    private FloatingActionButton searchFab;
    private TextView date;
    private TextView daysText;
    private TextView year;
    private TextView days;
    private TextView messengeText;

    public enum AnimationType {
        None, Inc, Dec, Today
    }

    @Inject
    public MainFragment() {

    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.takeView(this);
        this.presenter.initialize();
        this.presenter.initData(AnimationType.None);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.presenter.dropView();
    }

    @SuppressLint("ClickableViewAccessibility")
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

        messengeText = view.findViewById(R.id.messengeText);

        adapter = new CardAdapter(this,getContext());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshCards();
                swipeRefreshLayout.setRefreshing(false);    //TODO
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
                if(presenter.isBefore()) {              //To not over increment
                    if(adapter.getItemCount() > 0)      //If there is no events, do not animate
                        runFadeOutAnimation();

                    new Handler().postDelayed(new Runnable() {  //TODO
                        @Override
                        public void run() {     //Instantly after animation ends
                            presenter.incrementDay();

                            runShowFABS();
                        }
                    },animationFadeOut.getDuration() + 50);
                }
            }
        });


        decDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(presenter.isAfter()) {               //To not over decrement
                    if(adapter.getItemCount() > 0)      //If there is no events, do not animate
                        runFadeOutAnimation();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {    //Instantly after animation ends
                            presenter.decrementDay();

                            runShowFABS();
                        }
                    },animationFadeOut.getDuration() + 50);
                }
            }
        });

        searchFab = view.findViewById(R.id.searchFab);
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
                runShowFABS();

                new Handler().postDelayed(new Runnable() {  //TODO
                    @Override
                    public void run() {         //Instantly after animation ends
                        presenter.loadToday();
                    }
                },animationFadeOut.getDuration() + 50);
            }
        });

        waitToHideFABs();

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                runShowFABS();
                return false;
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
                    adapter.notifyDataSetChanged();
                }
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
    public void showCard(String date, int eventID) {
        Intent intent = new Intent(this.getContext(), EventActivity.class);
        intent.putExtra(EventActivity.EXTRA_EVENT_DATE, date);
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

    @SuppressLint("RestrictedApi")
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
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

                new Handler().postDelayed(new Runnable() {  //Because of flash of the screen at the end of the animation
                    @Override
                    public void run() {
                        recyclerView.setVisibility(View.GONE);  //Hide instantly after the animation
                    }
                },animationFadeOut.getDuration());
            }
        });

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
    public void hideMessenge() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messengeText.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showNoInternetConnection() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.notifyDataSetChanged();
                messengeText.setVisibility(View.VISIBLE);
                String msg = getResources().getString(R.string.no_connection);
                messengeText.setText(msg);

                incDayButton.setEnabled(true);
                decDayButton.setEnabled(true);
            }
        });
    }

    @Override
    public void showNoData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.clear();
                adapter.notifyDataSetChanged();
                messengeText.setVisibility(View.VISIBLE);
                String msg = getResources().getString(R.string.no_data);
                messengeText.setText(msg);

                incDayButton.setEnabled(true);
                decDayButton.setEnabled(true);
            }
        });
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

    @Override
    public void showIncDay() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runFromRightAnimation();
            }
        });
    }

    @Override
    public void showDecDay() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runFromLeftAnimation();
            }
        });
    }

    @Override
    public void showToday() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runSlideDownAnimation();
            }
        });
    }

    private void runHideFABS() {    //Run fadeout animation for both FABs, handles visibility of ToFirst FAB
        final Animation animation = animationFadeOut;
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationEnd(Animation animation) {
                searchFab.setVisibility(View.GONE);
                tofirstFab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        searchFab.startAnimation(animation);
        if(tofirstFab.getVisibility() != View.GONE)     //Do not animate on today
            tofirstFab.startAnimation(animation);
    }

    @SuppressLint("RestrictedApi")
    private void runShowFABS() {                        //Run fadein animation for both FABs, at the end run waitToHideFABs()
        if(searchFab.getVisibility() == View.GONE ) {   //To not run animation twice
            searchFab.setVisibility(View.VISIBLE);
            if(!presenter.isToday())
                tofirstFab.setVisibility(View.VISIBLE);
            Animation animation = animationFadeIn;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    waitToHideFABs();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            searchFab.startAnimation(animation);
            if(!presenter.isToday())
                tofirstFab.startAnimation(animation);
        }
    }

    private void waitToHideFABs() {                 //Wait 1500 milisec to hide FABs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runHideFABS();
            }
        },1000);
    }
}
