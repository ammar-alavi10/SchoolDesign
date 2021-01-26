package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;

public class TestSeries extends AppCompatActivity {

    String class_name, subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series);

        class_name = getIntent().getStringExtra("class_name");
        subject = getIntent().getStringExtra("subject_name");

        Toolbar toolbar = findViewById(R.id.notice_toolbar);
        toolbar.setTitle("Online Test Series");
        setSupportActionBar(toolbar);
    }

    public void MCQClicked(View view) {
        Intent intent = new Intent(TestSeries.this, TestList.class);
        intent.putExtra("class_name", class_name);
        intent.putExtra("subject_name", subject);
        intent.putExtra("type", "mcq");
        startActivity(intent);
    }

    public void FillinTheBlanks(View view) {
        Intent intent = new Intent(TestSeries.this, TestList.class);
        intent.putExtra("class_name", class_name);
        intent.putExtra("subject_name", subject);
        intent.putExtra("type", "blanks");
        startActivity(intent);
    }

    public void TrueOrFalse(View view) {
        Intent intent = new Intent(TestSeries.this, TestList.class);
        intent.putExtra("class_name", class_name);
        intent.putExtra("subject_name", subject);
        intent.putExtra("type", "truefalse");
        startActivity(intent);
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(TestSeries.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}