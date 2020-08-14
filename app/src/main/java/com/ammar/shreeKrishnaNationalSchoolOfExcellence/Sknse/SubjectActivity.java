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
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.AnnouncementListAdapter;
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

    private String subject_name, class_name;
    private List<AnnouncementModel> announcementModels;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        announcementModels = new ArrayList<>();

        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        category = preferences.getInt("category", -1);
        db = FirebaseFirestore.getInstance();

        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");

        Toolbar toolbar = findViewById(R.id.select_subject_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_view_subject_announcement);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InstantiateRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(category == 1)
        {
            getMenuInflater().inflate(R.menu.teachers_menu, menu);
        }
        else if(category == 2)
        {
            getMenuInflater().inflate(R.menu.subject_student_menu, menu);
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
                startActivity(intent);
                break;
            case R.id.notes_upload:
                intent = new Intent(SubjectActivity.this, UploadNotes.class);
                intent.putExtra("subject_name",subject_name);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
                break;
            case R.id.announcement:
                intent = new Intent(SubjectActivity.this, AddAnnouncement.class);
                intent.putExtra("subject_name",subject_name);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
                break;
            case R.id.notes_show:
                intent = new Intent(SubjectActivity.this, ShowNotesList.class);
                intent.putExtra("subject_name", subject_name);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
                break;
            case R.id.video_show:
                intent = new Intent(SubjectActivity.this, ShowVideoList.class);
                intent.putExtra("subject_name", subject_name);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
                break;
            case R.id.test_show:
            case R.id.show_test:
                intent = new Intent(SubjectActivity.this, TestList.class);
                intent.putExtra("subject_name", subject_name);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
                break;
            case R.id.add_test:
                intent = new Intent(SubjectActivity.this, MakeQuizActivity.class);
                intent.putExtra("subject_name", subject_name);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void InstantiateRecyclerView() {
        db.collection("announcements")
                .whereEqualTo("subject", subject_name + class_name)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("QueryResult", document.getId() + " => " + document.getData());
                            HashMap<String, String> model = (HashMap) document.getData();
                            AnnouncementModel announcementModel = new AnnouncementModel();
                            announcementModel.setTitle(model.get("title"));
                            announcementModel.setDescription(model.get("description"));
                            announcementModel.setDate(model.get("date"));
                            announcementModels.add(announcementModel);
                        }
                        setAdapter();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Loading Failed!! Try Again...", Toast.LENGTH_LONG).show();
                    Log.d("Category", "get failed with ", task.getException());
                }
            }
        });
    }

    private void setAdapter() {
        recyclerView.setAdapter(new AnnouncementListAdapter(announcementModels));
    }
}