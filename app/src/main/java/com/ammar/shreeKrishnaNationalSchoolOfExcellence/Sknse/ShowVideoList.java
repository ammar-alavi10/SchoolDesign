package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.VideoListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowVideoList extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<String> videoTitle, videoType, videoUrl;
    String subject, class_name, chapter_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_videolist);

        videoTitle = new ArrayList<>();
        videoType = new ArrayList<>();
        videoUrl = new ArrayList<>();
        subject = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        chapter_no = getIntent().getStringExtra("chapter_no");
        recyclerView = findViewById(R.id.recyclerview_ShowVideoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InstantiateRecyclerView();
    }

    private void InstantiateRecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("videos")
                .whereEqualTo("subject", subject + class_name + chapter_no)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult() != null)
                    {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("QueryResult", document.getId() + " => " + document.getData());
                            HashMap<String, String> videoModel = (HashMap) document.getData();
                            videoTitle.add(videoModel.get("name"));
                            videoType.add(videoModel.get("type"));
                            videoUrl.add(videoModel.get("videourl"));
                        }
                    }
                    setAdapter();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Loading Failed!! Try Again...", Toast.LENGTH_LONG).show();
                    Log.d("Category", "get failed with ", task.getException());
                }
            }
        });
    }

    private void setAdapter() {
        VideoListAdapter.RecyclerVideoViewClickListener listener = new VideoListAdapter.RecyclerVideoViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ShowVideo.class);
                intent.putExtra("videotitle", videoTitle.get(position));
                intent.putExtra("videotype", videoType.get(position));
                intent.putExtra("videourl", videoUrl.get(position));
                startActivity(intent);
            }
        };
        recyclerView.setAdapter(new VideoListAdapter(videoTitle, videoType, videoUrl, listener));
    }

}