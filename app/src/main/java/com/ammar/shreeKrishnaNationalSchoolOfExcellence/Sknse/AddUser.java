package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.NewUserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddUser extends AppCompatActivity {

    EditText class_et, rollno_et, yoa_et, dob_et, name_et, username_et;
    String class_name, rollno, yoa, dob, name, username;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        class_et = findViewById(R.id.class_et);
        rollno_et = findViewById(R.id.rollno_et);
        yoa_et = findViewById(R.id.yoa_et);
        dob_et = findViewById(R.id.dob_et);
        name_et = findViewById(R.id.name_et);
        username_et = findViewById(R.id.username_et);

        mAuth = FirebaseAuth.getInstance();

    }

    public void Add(View view) {
        username = username_et.getText().toString();
        class_name = class_et.getText().toString();
        rollno = rollno_et.getText().toString();
        yoa = yoa_et.getText().toString();
        dob = dob_et.getText().toString();
        name = name_et.getText().toString();

        if(username.equals("") || class_name.equals("") || rollno.equals("") || yoa.equals("") || dob.equals("") || name.equals(""))
        {
            Toast.makeText(AddUser.this, "All Fields are required", Toast.LENGTH_LONG).show();
        }
        else {
            final HashMap<String, Object> userModel = new HashMap<>();
            userModel.put("class", class_name);
            userModel.put("dob", dob);
            userModel.put("email", username);
            userModel.put("yearofadmission", yoa);
            userModel.put("category", 2);
            userModel.put("rollno", rollno);
            userModel.put("name", name);
            mAuth.createUserWithEmailAndPassword(username, name + rollno).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getUser() != null)
                    {
                        String uid = task.getResult().getUser().getUid();
                        userModel.put("uid", uid);
                        FirebaseFirestore.getInstance().collection("users").document(uid)
                                .set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(AddUser.this, "User Added", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(AddUser.this, "Problem in adding user", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(AddUser.this, "Problem in adding user", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}