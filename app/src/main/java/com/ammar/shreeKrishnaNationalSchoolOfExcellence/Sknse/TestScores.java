package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.TestListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.TestScoreAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.TestModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.TestResultModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestScores extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<TestResultModel> testModels ;
    String subject, class_name, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scores);

        testModels = new ArrayList<>();

        subject = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = findViewById(R.id.score_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InstantiateRecyclerView();

    }

    private void InstantiateRecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.d("TestScore", subject + class_name + uid);
        db.collection("testscores")
                .whereEqualTo("subject_name", subject + class_name + uid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (final QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("QueryResult", document.getId() + " => " + document.getData());
                            Map<String, Object> test = document.getData();
                            TestResultModel testModel = new TestResultModel();
                            testModel.setTitle((String) test.get("title"));
                            Long correct = (Long) test.get("correct");
                            testModel.setCorrect(correct.intValue());
                            Long wrong = (Long) test.get("wrong");
                            testModel.setWrong(wrong.intValue());
                            Long total = (Long) test.get("total");
                            testModel.setTotal(total.intValue());
                            Long unanswered = (Long) test.get("unanswered");
                            testModel.setUnanswered(unanswered.intValue());
                            testModels.add(testModel);

                        }
                    }
                    recyclerView.setAdapter(new TestScoreAdapter(testModels));
                } else {
                    Toast.makeText(getApplicationContext(), "Loading Failed!! Try Again...", Toast.LENGTH_LONG).show();
                    Log.d("Category", "get failed with ", task.getException());
                }
            }
        });
    }

}