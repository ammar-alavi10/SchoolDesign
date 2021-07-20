package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.AnnouncementListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.ChatMainActivity;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.AnnouncementModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private String subject_name, class_name, chapter_no;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        category = preferences.getInt("category", -1);

        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        chapter_no = "chapter" + getIntent().getStringExtra("chapter_no");

        Toolbar toolbar = findViewById(R.id.select_subject_toolbar);
        toolbar.setTitle(subject_name);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(category == 1)
        {
            getMenuInflater().inflate(R.menu.teachers_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.video_upload:
                Intent intent = new Intent(SubjectActivity.this, UploadVideoActivity.class);
                intent.putExtra("subject_name",subject_name);
                intent.putExtra("class_name", class_name);
                intent.putExtra("chapter_no", chapter_no);
                startActivity(intent);
                break;
            case R.id.notes_upload:
                intent = new Intent(SubjectActivity.this, UploadNotes.class);
                intent.putExtra("subject_name",subject_name);
                intent.putExtra("class_name", class_name);
                intent.putExtra("chapter_no", chapter_no);
                startActivity(intent);
                break;
            case R.id.announcement:
                intent = new Intent(SubjectActivity.this, AddAnnouncement.class);
                intent.putExtra("subject_name",subject_name);
                intent.putExtra("class_name", class_name);
                intent.putExtra("chapter_no", chapter_no);
                startActivity(intent);
                break;
            case R.id.add_test:
                intent = new Intent(SubjectActivity.this, TestSeries.class);
                intent.putExtra("subject_name", subject_name);
                intent.putExtra("class_name", class_name);
                intent.putExtra("chapter_no", chapter_no);
                intent.putExtra("isTestAdd", true);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ChaptersTestClicked(View view) {
        Intent intent = new Intent(SubjectActivity.this, TestSeries.class);
        intent.putExtra("subject_name", subject_name);
        intent.putExtra("class_name", class_name);
        intent.putExtra("chapter_no", chapter_no);
        startActivity(intent);
    }

    public void VideosClicked(View view) {
        Intent intent = new Intent(SubjectActivity.this, ShowVideoList.class);
        intent.putExtra("subject_name", subject_name);
        intent.putExtra("class_name", class_name);
        intent.putExtra("chapter_no", chapter_no);
        startActivity(intent);
    }

    public void NotesClicked(View view) {
        Intent intent = new Intent(SubjectActivity.this, ShowNotesList.class);
        intent.putExtra("subject_name", subject_name);
        intent.putExtra("class_name", class_name);
        intent.putExtra("chapter_no", chapter_no);
        startActivity(intent);
    }

    public void AnnouncementClicked(View view) {
        Intent intent = new Intent(SubjectActivity.this, ShowAnnouncementList.class);
        intent.putExtra("subject_name",subject_name);
        intent.putExtra("class_name", class_name);
        intent.putExtra("chapter_no", chapter_no);
        startActivity(intent);
    }

    public void ChatClicked(View view) {
        Intent intent = new Intent(SubjectActivity.this, ChatMainActivity.class);
        intent.putExtra("class_name", class_name);
        startActivity(intent);
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(SubjectActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}