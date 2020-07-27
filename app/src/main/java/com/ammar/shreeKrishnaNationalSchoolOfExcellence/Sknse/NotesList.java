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

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.NotesListAdapter;
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

public class NotesList extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<String> notesTitle, notesUrl;
    String subject, class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);

        notesTitle = new ArrayList<>();
        notesUrl = new ArrayList<>();

        subject = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");

        recyclerView = findViewById(R.id.recyclerview_ShowNotesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InstantiateRecyclerView();
    }

    private void InstantiateRecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("notes")
                .whereEqualTo("subject", subject + class_name)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("QueryResult", document.getId() + " => " + document.getData());
                            HashMap<String, String> videoModel = (HashMap) document.getData();
                            notesTitle.add(videoModel.get("name"));
                            notesUrl.add(videoModel.get("notesurl"));
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
        NotesListAdapter.RecyclerNotesViewClickListener listener = new NotesListAdapter.RecyclerNotesViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ShowNotes.class);
                intent.putExtra("notestitle", notesTitle.get(position));
                intent.putExtra("notesurl", notesUrl.get(position));
                startActivity(intent);
            }
        };
        recyclerView.setAdapter(new NotesListAdapter(notesTitle, notesUrl, listener));
    }
}