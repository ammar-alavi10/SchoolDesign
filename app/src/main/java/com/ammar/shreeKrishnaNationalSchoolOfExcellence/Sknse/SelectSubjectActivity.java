package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SelectSubjectActivity extends AppCompatActivity {

    private RecyclerView classesView;
    private List<Subject> subjects;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private FirebaseUser user;
    private SubjectAdapter.RecyclerViewClickListener listener;
    int[] class_bg = new int[]{
            R.drawable.books_bg_1,
            R.drawable.books_bg_2,
            R.drawable.books_bg_3,
            R.drawable.books_bg_4,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);

        toolbar = findViewById(R.id.select_subject_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        classesView = findViewById(R.id.classes_recyclerView);
        classesView.setLayoutManager(new LinearLayoutManager(this));
        InstantiateRecyclerView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_subject_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                LogoutUser();
                break;
            case R.id.refresh:
                InstantiateRecyclerView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void LogoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(SelectSubjectActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
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
                        subjects = subjectDocument.getSubjects();
                        setAdapter();
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
        listener = new SubjectAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                intent.putExtra("subject_name", subjects.get(position).subject_name);
                intent.putExtra("class_name", subjects.get(position).class_name);
                startActivity(intent);
            }
        };

        classesView.setAdapter(new SubjectAdapter(subjects, class_bg, listener));

    }


}