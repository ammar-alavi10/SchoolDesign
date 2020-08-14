package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.TestListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.TestModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestList extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<TestModel> testModels ;
    String subject, class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        testModels = new ArrayList<>();

        subject = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");

        recyclerView = findViewById(R.id.test_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InstantiateRecyclerView();
    }

    private void InstantiateRecyclerView() {
        testModels = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("test")
                .whereEqualTo("subject_name", subject + class_name)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("QueryResult", document.getId() + " => " + document.getData());
                            Map<String, Object> test = document.getData();
                            TestModel testModel = new TestModel();
                            Log.d("Test", (String) test.get("testTitle"));
                            testModel.setTestTitle((String) test.get("testTitle"));
                            testModel.setTestTime((String) test.get("testTime"));
                            testModels.add(testModel);
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

        TestListAdapter.TestListRecyclerListener listener = new TestListAdapter.TestListRecyclerListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                intent.putExtra("test_title", testModels.get(position).getTestTitle());
                intent.putExtra("subject_name", subject);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View v, final int position) {

                SharedPreferences preferences = getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
                int category = preferences.getInt("category", -1);
                if(category == 1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestList.this);
                    builder.setTitle("Confirm Delete !");
                    builder.setMessage("You are about to delete this test. Do you really want to proceed ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("test").document(subject + class_name + testModels.get(position).getTestTitle())
                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        InstantiateRecyclerView();
                                        Toast.makeText(getApplicationContext(), "Test Deleted", Toast.LENGTH_SHORT).show();
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
        Log.d("Test", "Above");
        recyclerView.setAdapter(new TestListAdapter(testModels, listener));
    }
}