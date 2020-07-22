package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.activity.SplashScreen;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page_layout);
    }

    public void onKidsCornerPressed(View view) {
        startActivity(new Intent(StartActivity.this, SplashScreen.class));
    }
}