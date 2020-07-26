package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class SubjectActivity extends AppCompatActivity {

    String subject, class_name;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        subject = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");

        toolbar = findViewById(R.id.select_subject_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teachers_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.video_upload:
                Intent intent = new Intent(SubjectActivity.this, UploadVideoActivity.class);
                intent.putExtra("subject_name",subject);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
                break;
            case R.id.notes_upload:
                // goto upload notes
                break;
            case R.id.announcement:
                // add announcement
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}