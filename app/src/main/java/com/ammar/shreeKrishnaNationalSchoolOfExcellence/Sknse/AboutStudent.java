package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class AboutStudent extends AppCompatActivity {

    private ImageView circularImage;
    private String rollno, class_name, name, dob, yoa;
    private TextView roll_tv, name_tv, dob_tv, yoa_tv, class_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_student);

        circularImage = findViewById(R.id.student_image);
        name_tv = findViewById(R.id.student_name);
        dob_tv = findViewById(R.id.student_dob);
        yoa_tv = findViewById(R.id.yoa);
        class_tv = findViewById(R.id.student_class);


        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null)
                    {
                        try {
                            name = documentSnapshot.getString("name");
                            rollno = documentSnapshot.getString("rollno");
                            class_name = documentSnapshot.getString("class");
                            yoa = documentSnapshot.getString("yearofadmission");
                            dob = documentSnapshot.getString("dob");

                            name_tv.setText(name);
                            yoa_tv.setText("Year of Admission : " + yoa);
                            dob_tv.setText("Date of Birth : " + dob);
                            class_tv.setText("Class : " + class_name);
                        }
                        catch (Exception e)
                        {
                            Log.d("AboutStudent", e.toString());
                        }

                        FirebaseStorage.getInstance().getReference().
                                child("student-photos/" + class_name + "-" + rollno + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(circularImage);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("ImageLoading", e.toString());
                                Toast.makeText(AboutStudent.this, "Error in loading image", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(AboutStudent.this, "Error in loading data", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(AboutStudent.this, "Error in loading data", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void FeeManage(View view) {
        startActivity(new Intent(AboutStudent.this, FeeActivity.class));
    }

    public void FeedbackClicked(View view) {
    }

    public void BirthdayCard(View view) {
        startActivity(new Intent(AboutStudent.this, Birthday.class));
    }

    public void AttendanceManagement(View view) {
        startActivity(new Intent(AboutStudent.this, AttendanceManagement.class));
    }

    public void LeaveAppl(View view) {
        startActivity(new Intent(AboutStudent.this, LeaveApplication.class));
    }
}