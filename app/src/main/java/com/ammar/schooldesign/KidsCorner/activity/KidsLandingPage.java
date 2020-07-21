package com.ammar.schooldesign.KidsCorner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ammar.schooldesign.KidsCorner.balloonpoper.BalloonMain;
import com.ammar.schooldesign.R;
import com.ammar.schooldesign.KidsCorner.fragment.MultiplicationTableFragment;
import com.ammar.schooldesign.KidsCorner.fragment.SlideFragment;

public class KidsLandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_landing_page);

    }

    public void onLearnPressed(View view) {
        Intent intent = new Intent(KidsLandingPage.this, MainActivity.class);
        startActivity(intent);
    }

    public void onDrawPressed(View view) {
        startActivity(new Intent(getApplicationContext(), PaintActivity.class));
    }

    public void onGuessPressed(View view) {
        Fragment fragment = new SlideFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.kids_activity, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void onNumbersPressed(View view) {
        Fragment fragment = new MultiplicationTableFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.kids_activity, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public void onBalloonPopperPressed(View view) {
        startActivity(new Intent(getApplicationContext(), BalloonMain.class));
    }
}