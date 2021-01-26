package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Log.d("About", "in about us");
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(AboutUs.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}