package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.activity.KidsLandingPage;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainScreen extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        Toolbar toolbar = findViewById(R.id.select_subject_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_subject_student_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
            case R.id.logout_student:
                LogoutUser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void LogoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(MainScreen.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    public void ElearningClicked(View view) {
        startActivity(new Intent(MainScreen.this, SelectSubjectActivity.class));
    }

    public void AboutClicked(View view) {
        startActivity(new Intent(MainScreen.this, AboutStudent.class));
    }

    public void TopperClicked(View view) {
    }

    public void ClassDiaryClicked(View view) {
        startActivity(new Intent(MainScreen.this, DiaryActivity.class));
    }

    public void ReportCardClicked(View view) {
        Intent intent = new Intent(MainScreen.this, SelectSubjectActivity.class);
        intent.putExtra("isTestScore", true);
        startActivity(intent);
    }

    public void TestSeriesClicked(View view) {
        Intent intent = new Intent(MainScreen.this, SelectSubjectActivity.class);
        intent.putExtra("isTest", true);
        startActivity(intent);
    }

    public void HeadlinesClicked(View view) {
        startActivity(new Intent(MainScreen.this, HeadlinesActivity.class));
    }

    public void LibraryClicked(View view) {
        startActivity(new Intent(MainScreen.this, LibraryActivity.class));
    }

    public void TrackingClicked(View view) {
        startActivity(new Intent(MainScreen.this, Tracking.class));
    }

    public void KidsCornerClicked(View view) {
        startActivity(new Intent(MainScreen.this, KidsLandingPage.class));
    }

    public void AlertClicked(View view) {
    }

    public void LiveClassClicked(View view) {
        startActivity(new Intent(MainScreen.this, LiveVideoList.class));
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(MainScreen.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}