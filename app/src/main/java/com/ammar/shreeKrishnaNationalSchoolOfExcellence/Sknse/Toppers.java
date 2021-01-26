package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class Toppers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toppers);


    }

    public void ClassToppers(View view) {
        startActivity(new Intent(Toppers.this, ClassToppers.class));
    }

    public void motivational(View view) {
        startActivity(new Intent(Toppers.this, MotivationalVideo.class));
    }

    public void studytips(View view) {
        startActivity(new Intent(Toppers.this, StudyTips.class));
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(Toppers.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}