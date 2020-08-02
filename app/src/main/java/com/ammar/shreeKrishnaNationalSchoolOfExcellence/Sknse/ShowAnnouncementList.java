package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

public class ShowAnnouncementList extends AppCompatActivity {

    String class_name, subject_name;
    List<AnnouncementModel> announcementModels;
    RecyclerView recyclerView;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_announcement_list);

        announcementModels = new ArrayList<>();

        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");

        recyclerView = findViewById(R.id.announcement_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InstantiateRecyclerView();
    }

    private void InstantiateRecyclerView() {
        db = FirebaseFirestore.getInstance();
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
}