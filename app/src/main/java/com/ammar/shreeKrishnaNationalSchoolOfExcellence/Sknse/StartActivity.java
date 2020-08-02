package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.activity.SplashScreen;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth mAuth;
    TextView logintv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page_layout);

        logintv = findViewById(R.id.profile_text);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        if(user != null)
        {
            logintv.setText("Profile");
        }
        else{
            logintv.setText("Login");
        }
    }

    public void onKidsCornerPressed(View view) {
        startActivity(new Intent(StartActivity.this, SplashScreen.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null)
        {
            logintv.setText("Profile");
        }
        else{
            logintv.setText("Login");
        }
    }

    public void loginPressed(View view) {
        if(user != null)
        {
            startActivity(new Intent(StartActivity.this, SelectSubjectActivity.class));
        }
        else {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
        }
    }

    public void AboutUsPressed(View view) {
        startActivity(new Intent(StartActivity.this, AboutUs.class));
    }
}