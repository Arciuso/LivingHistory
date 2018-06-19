package com.example.arcius.livinghistory.intro.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.intro.IntroContract;
import com.example.arcius.livinghistory.intro.IntroPresenter;

public class IntroFragment extends Fragment implements IntroContract.View.Intro {

    private TextView introText;

    public IntroFragment newInstance() {
        return new IntroFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.intro_frag, container, false);

        this.introText = view.findViewById(R.id.introText);

        showIntroText();

        return view;
    }

    @Override
    public void showIntroText() {
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in_slow);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideIntroText();
                    }
                }, 500); //Wait 5 seconds
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.introText.setAnimation(fadeIn);
    }

    @Override
    public void hideIntroText() {
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out_slow);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                introText.setVisibility(View.INVISIBLE);
                showIntroSettings();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.introText.startAnimation(fadeOut);
    }

    @Override
    public void showIntroSettings() {
        SettingsFragment fragment = new SettingsFragment().newInstance();
        new IntroPresenter(fragment);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFrame, fragment);
        transaction.commit();
    }

    private void performTransition() {

    }
}
