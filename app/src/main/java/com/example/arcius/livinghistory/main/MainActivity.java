package com.example.arcius.livinghistory.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.arcius.livinghistory.R;
import com.example.arcius.livinghistory.intro.IntroActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_act);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(preferences.getBoolean("firstrun",true)){
            preferences.edit().putBoolean("firstrun", true).apply(); //TODO put it on the end of the intro phase, to not ask again
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
    }
}
