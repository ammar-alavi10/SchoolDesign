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

    String class_name, subject, chapter_no;
    Boolean isTestAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_series);

        class_name = getIntent().getStringExtra("class_name");
        subject = getIntent().getStringExtra("subject_name");
        chapter_no = getIntent().getStringExtra("chapter_no");
        isTestAdd = getIntent().getBooleanExtra("isTestAdd", false);

        Toolbar toolbar = findViewById(R.id.notice_toolbar);
        toolbar.setTitle("Online Test Series");
        setSupportActionBar(toolbar);
    }

    public void MCQClicked(View view) {
        OpenTestActivity("mcq");
    }

    public void FillInTheBlanks(View view) {
        OpenTestActivity("blanks");
    }

    public void TrueOrFalse(View view) {
        OpenTestActivity("truefalse");
    }

    private void OpenTestActivity(String testType) {
        Intent intent;
        if(isTestAdd)
            intent = new Intent(TestSeries.this, MakeQuizActivity.class);
        else
            intent = new Intent(TestSeries.this, TestList.class);
        intent.putExtra("class_name", class_name);
        intent.putExtra("subject_name", subject);
        intent.putExtra("chapter_no", chapter_no);
        intent.putExtra("type", testType);
        startActivity(intent);
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(TestSeries.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}