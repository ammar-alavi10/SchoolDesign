package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.ChapterAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.SubjectAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.Chapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.Subject;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterActivity extends AppCompatActivity {

    private RecyclerView classesView;
    private List<Chapter> chapters;
    private FirebaseUser user;

    private int category;
    private String subject_name, class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        category = preferences.getInt("category", -1);

        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");

        chapters = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.select_subject_toolbar);
        toolbar.setTitle(subject_name);
        setSupportActionBar(toolbar);

        classesView = findViewById(R.id.chapter_recyclerView);
        classesView.setLayoutManager(new GridLayoutManager(this, 2));

        InstantiateRecyclerViewStudent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(category == 1) {
            getMenuInflater().inflate(R.menu.add_chapter_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_chapter:
                openDialog();

        }
        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ChapterActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.chapter_dialog_layout, null);

        final EditText chapterNo = mView.findViewById(R.id.et_chapter_no);
        final EditText chapterName = mView.findViewById(R.id.et_chapter_name);
        Button upload_bt = mView.findViewById(R.id.btn_yes);
        Button cancel_bt = mView.findViewById(R.id.btn_no);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();

        upload_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chapter_name = chapterName.getText().toString();
                String chapter_no = chapterNo.getText().toString();

                if(TextUtils.isEmpty(chapter_no))
                {
                    chapterNo.setError("Enter Chapter Number");
                    chapterNo.requestFocus();
                }
                else if(TextUtils.isEmpty(chapter_name))
                {
                    chapterName.setError("Enter Chapter Name");
                    chapterName.requestFocus();
                }
                else {
                    Map<String, Object> mp = new HashMap<>();
                    mp.put("chapter_name", chapter_name);
                    mp.put("chapter_no", chapter_no);
                    mp.put("class_name", class_name);
                    mp.put("subject_name", subject_name);
                    Map<String, Object> map = new HashMap<>();
                    map.put(chapter_no, mp);
                    FirebaseFirestore db1 = FirebaseFirestore.getInstance();
                    db1.collection("chapters").document(subject_name + class_name)
                            .set(map, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ChapterActivity.this, "Chapter Added", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChapterActivity.this, "Problem in adding chapter", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });

        cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void InstantiateRecyclerViewStudent() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference documentReference = db.collection("chapters").document(subject_name+class_name);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if( documentSnapshot != null && documentSnapshot.exists()) {
                        Map<String, Object> map =  documentSnapshot.getData();
                        if (map != null) {
                            for (Map.Entry<String, Object> entry : map.entrySet()) {

                                Map<String, String> map1= ((Map<String, String>) entry.getValue());
                                Chapter chapter = new Chapter();
                                chapter.setChapter_no(map1.get("chapter_no"));
                                chapter.setChapter_name(map1.get("chapter_name"));
                                chapters.add(chapter);
                            }
                        }
                        setAdapter();
                    }
                }
            }
        });
    }

    private void setAdapter() {
        SubjectAdapter.RecyclerViewClickListener listener = new SubjectAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), SubjectActivity.class);
                intent.putExtra("subject_name", subject_name);
                intent.putExtra("class_name", class_name);
                intent.putExtra("chapter_no", chapters.get(position).getChapter_no());
                startActivity(intent);
            }
        };

        classesView.setAdapter(new ChapterAdapter(chapters, listener));

    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(ChapterActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}