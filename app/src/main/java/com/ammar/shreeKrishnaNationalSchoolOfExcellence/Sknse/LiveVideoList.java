package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.VideoListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LiveVideoList extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<String> videoTitle, videoType, videoUrl;
    EditText class_et;
    String class_name;
    int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_video_list);

        Toolbar toolbar = findViewById(R.id.notice_toolbar);
        toolbar.setTitle("Live Classes");
        setSupportActionBar(toolbar);

        videoTitle = new ArrayList<>();
        videoType = new ArrayList<>();
        videoUrl = new ArrayList<>();

        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        category = preferences.getInt("category", -1);

        recyclerView = findViewById(R.id.recyclerview_ShowVideoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        class_et = findViewById(R.id.class_et);

        if(category == 1)
        {
            class_name = preferences.getString("class_name", null);
            InstantiateRecyclerView();
        }
    }

    private void InstantiateRecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("livevideos")
                .whereEqualTo("subject", class_name)
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
                Intent intent = new Intent(getApplicationContext(), LiveClass.class);
                intent.putExtra("videotitle", videoTitle.get(position));
                intent.putExtra("videotype", videoType.get(position));
                intent.putExtra("videourl", videoUrl.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View v, final int position) {
                SharedPreferences preferences = getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
                int category = preferences.getInt("category", -1);
                if(category == 1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LiveVideoList.this);
                    builder.setTitle("Confirm Delete !");
                    builder.setMessage("You are about to delete this Video. Do you really want to proceed ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("livevideos").whereEqualTo("name", videoTitle.get(position)).whereEqualTo("subject", class_name).
                                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        if (task.getResult() != null) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String id = document.getId();
                                                db.collection("livevideos").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            InstantiateRecyclerView();
                                                            Toast.makeText(getApplicationContext(), "Video Deleted", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            }
        };
        recyclerView.setAdapter(new VideoListAdapter(videoTitle, videoType, videoUrl, listener));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Category", " I ");
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Log.d("Category", category + " ");
            if(category == 1)
            {
                Log.d("Category", "Inside");
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
                startActivity(new Intent(LiveVideoList.this, AddNotice.class));
        }
        return super.onOptionsItemSelected(item);

    }

    public void ShowVideos(View view) {
        class_name = class_et.getText().toString();
        if(TextUtils.isEmpty(class_name))
        {
            class_et.setError("Enter class");
            class_et.requestFocus();
        }
        else{
            InstantiateRecyclerView();
        }
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(LiveVideoList.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}