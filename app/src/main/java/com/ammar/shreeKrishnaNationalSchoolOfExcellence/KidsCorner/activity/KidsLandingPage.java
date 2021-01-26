package com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.balloonpoper.BalloonMain;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.fragment.SlideFragment;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse.StartActivity;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse.Toppers;

public class KidsLandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_landing_page);

    }

    @Override
    public void onResume() {
        super.onResume();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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

    public void onBalloonPopperPressed(View view) {
        startActivity(new Intent(getApplicationContext(), BalloonMain.class));
    }

    public void onImageSlidePressed(View view) {
        startActivity(new Intent(KidsLandingPage.this, ImageGame.class));
    }

    public void onTracePressed(View view) {
        startActivity(new Intent(KidsLandingPage.this, TraceActivity.class));
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(KidsLandingPage.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}