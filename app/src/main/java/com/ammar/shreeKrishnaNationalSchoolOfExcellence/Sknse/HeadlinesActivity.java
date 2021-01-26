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
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.AnnouncementModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HeadlinesActivity extends AppCompatActivity {

    List<AnnouncementModel> announcementModels;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);

        Toolbar toolbar = findViewById(R.id.notice_toolbar);
        setSupportActionBar(toolbar);

        announcementModels = new ArrayList<>();

        recyclerView = findViewById(R.id.announcement_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null)
                    {
                        class_name = documentSnapshot.getString("class");
                        InstantiateRecyclerView();
                    }
                }
            }
        });

    }

    private void InstantiateRecyclerView() {
        db = FirebaseFirestore.getInstance();
        db.collection("headlines")
                .whereEqualTo("subject", class_name)
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
                    }
                    setAdapter();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
            SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
            int category = preferences.getInt("category", -1);
            if(category == 1)
            {
                getMenuInflater().inflate(R.menu.notice_menu, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_notice:
                Intent intent = new Intent(HeadlinesActivity.this, MakeQuizActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    public void CalendarClicked(View view) {
    }

    public void TimeTableClicked(View view) {
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(HeadlinesActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}