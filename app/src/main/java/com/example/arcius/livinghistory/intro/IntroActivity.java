package com.example.arcius.livinghistory.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.intro.Fragments.IntroFragment;

import net.danlew.android.joda.JodaTimeAndroid;

public class IntroActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_act);

        JodaTimeAndroid.init(this);

        loadIntroFragment();
    }

    @Override
    public void onBackPressed() {   //TODO Check if Fragments stack is not (empty?), if it's on the first then quit, else get back.
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void loadIntroFragment() {
        IntroFragment introFragment = (IntroFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame); // NOT THAT PROBLEM
        if (introFragment == null) {

            introFragment = new IntroFragment().newInstance();

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.contentFrame, introFragment);
            transaction.commit();
        }
    }
}
