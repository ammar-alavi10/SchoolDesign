package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ClassToppers extends AppCompatActivity {

    TextView name_tv1, name_tv2, name_tv3, name_tv4, name_tv5;
    TextView marks_1, marks_2, marks_3, marks_4, marks_5;
    String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_toppers);

        name_tv1 = findViewById(R.id.tv_1);
        name_tv2 = findViewById(R.id.tv_2);
        name_tv3 = findViewById(R.id.tv_3);
        name_tv4 = findViewById(R.id.tv_4);
        name_tv5 = findViewById(R.id.tv_5);

        marks_1 = findViewById(R.id.marks_1);
        marks_2 = findViewById(R.id.marks_2);
        marks_3 = findViewById(R.id.marks_3);
        marks_4 = findViewById(R.id.marks_4);
        marks_5 = findViewById(R.id.marks_5);

        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        class_name = preferences.getString("class_name", null);

        init();

    }

    private void init() {
        for(int i = 1; i < 6 ; i++) {
            final int finalI = i;
            FirebaseFirestore.getInstance().collection("topper" + class_name).document("topper" + i)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot != null) {
                            try {
                                if (finalI == 1) {
                                    name_tv1.setText(documentSnapshot.getString("name").toUpperCase());
                                    marks_1.setText(documentSnapshot.getString("marks"));
                                }
                                else if (finalI == 2) {
                                    name_tv2.setText(documentSnapshot.getString("name").toUpperCase());
                                    marks_2.setText(documentSnapshot.getString("marks"));
                                }
                                else if (finalI == 3) {
                                    name_tv3.setText(documentSnapshot.getString("name").toUpperCase());
                                    marks_3.setText(documentSnapshot.getString("marks"));
                                }
                                else if (finalI == 4) {
                                    name_tv4.setText(documentSnapshot.getString("name").toUpperCase());
                                    marks_4.setText(documentSnapshot.getString("marks"));
                                }
                                else {
                                    name_tv5.setText(documentSnapshot.getString("name").toUpperCase());
                                    marks_5.setText(documentSnapshot.getString("marks"));
                                }
                            } catch (Exception e) {
                                Log.d("Exception", e.toString());
                            }
                        }
                    }
                }
            });
        }
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(ClassToppers.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}