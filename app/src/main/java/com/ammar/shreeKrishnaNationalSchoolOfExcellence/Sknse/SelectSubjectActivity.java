package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.SubjectAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.Subject;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.SubjectDocument;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import java.util.List;

public class SelectSubjectActivity extends AppCompatActivity {

    private RecyclerView classesView;
    private List<Subject> subjects;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private int category;
    private String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);

        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        category = preferences.getInt("category", -1);
        Toolbar toolbar = findViewById(R.id.select_subject_toolbar);
        toolbar.setTitle("Select Subject");
        setSupportActionBar(toolbar);

        subjects = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        classesView = findViewById(R.id.classes_recyclerView);
        classesView.setLayoutManager(new GridLayoutManager(this, 2));
        if(category == 1)
        {
            InstantiateRecyclerView();
        }
        else if(category == 2)
        {
            class_name = preferences.getString("class_name", null);
            InstantiateRecyclerViewStudent();
        }
    }

    private void InstantiateRecyclerViewStudent() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("class").document(class_name);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if( documentSnapshot != null && documentSnapshot.exists()) {
                        List<String> subjectList = (List<String>) documentSnapshot.get("subjects");
                        if (subjectList != null) {
                            for (String s: subjectList)
                            {
                                Subject subject = new Subject();
                                subject.setSubject_name(s);
                                subject.setClass_name(class_name);
                                subjects.add(subject);
                            }
                            setAdapter();
                        }
                    }
                }
            }
        });
    }


    private void InstantiateRecyclerView() {
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        SubjectDocument subjectDocument = document.toObject(SubjectDocument.class);
                        if (subjectDocument != null) {
                            subjects = subjectDocument.getSubjects();
                            setAdapter();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Loading Failed!! Try Again...", Toast.LENGTH_LONG).show();
                        Log.d("Category", "No such document");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Loading Failed!! Try Again...", Toast.LENGTH_LONG).show();
                    Log.d("Category", "get failed with ", task.getException());
                }
            }
        });
    }

    private void setAdapter() {
        SubjectAdapter.RecyclerViewClickListener listener = new SubjectAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
                intent.putExtra("subject_name", subjects.get(position).getSubject_name());
                intent.putExtra("class_name", subjects.get(position).getClass_name());
                startActivity(intent);
            }
        };

        classesView.setAdapter(new SubjectAdapter(subjects, listener));

    }


}