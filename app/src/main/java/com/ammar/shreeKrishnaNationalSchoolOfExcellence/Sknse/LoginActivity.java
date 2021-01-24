package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout dropdown;
    private SharedPreferences preferences;
    private EditText userIdText;
    private EditText passwordText;
    private FirebaseAuth mAuth;
    private int category;

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            updateUI(currentUser);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        dropdown = findViewById(R.id.text_input_layout);
        AutoCompleteTextView dropdowntext = findViewById(R.id.dropdown_text);
        userIdText = findViewById(R.id.username_et);
        passwordText = findViewById(R.id.password_et);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null)
        {
            updateUI(user);
        }
        String[] categories = new String[]{
                "Admin",
                "Teacher",
                "Student",
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                LoginActivity.this,
                R.layout.dropdown_menu,
                categories
        );
        dropdowntext.setAdapter(adapter);
        dropdowntext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                category = i;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("category", category);
                editor.apply();
                userIdText.requestFocus();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        category = preferences.getInt("category", -1);
        String uid = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        if(category == ((Long) document.get("category")).intValue())
                        {
                            if(category == 0)
                            {
                                startActivity(new Intent(LoginActivity.this, AdminPanel.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(LoginActivity.this, SelectSubjectActivity.class));
                                finish();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Failed!! Try Again...", Toast.LENGTH_LONG).show();
                        Log.d("Category", "No such document");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Login Failed!! Try Again...", Toast.LENGTH_LONG).show();
                    Log.d("Category", "get failed with ", task.getException());
                }
            }
        });
    }

    public void onLoginPressed(View view) {
        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Logging in");
        String username = userIdText.getText().toString().trim();
        String password = passwordText.getText().toString();
        if(TextUtils.isEmpty(username))
        {
            userIdText.requestFocus();
            userIdText.setError("Enter Email-Id");
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            passwordText.requestFocus();
            passwordText.setError("Enter password");
            return;
        }
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d("Login", "Successful");
                    FirebaseUser user = mAuth.getCurrentUser();
                    String uid;
                    if (user != null) {
                        uid = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference docRef = db.collection("users").document(uid);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        if(category == ((Long) document.get("category")).intValue())
                                        {
                                            if(category == 0)
                                            {
                                                progressDialog.dismiss();
                                                startActivity(new Intent(LoginActivity.this, AdminPanel.class));
                                                finish();
                                                return;
                                            }
                                            else if(category == 2)
                                            {
                                                String class_name = (String) document.get("class");
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("class_name", class_name);
                                                editor.apply();
                                            }
                                            FirebaseMessaging.getInstance().subscribeToTopic("sknse").
                                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d("topic", e.getStackTrace().toString());
                                                        }
                                                    });
                                            progressDialog.dismiss();
                                            startActivity(new Intent(LoginActivity.this, MainScreen.class));
                                            finish();
                                        }
                                        else{
                                            mAuth.signOut();
                                            progressDialog.dismiss();
                                            ShowDialog("Login Failed!! Incorrect category...");
                                        }
                                    } else {
                                        progressDialog.dismiss();
                                        mAuth.signOut();
                                        ShowDialog("Login Failed!! Try Again...");
                                        Log.d("Category", "No such document");
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    mAuth.signOut();
                                    ShowDialog("Login Failed!! Try Again...");
                                    Log.d("Category", "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
                else{
                    progressDialog.dismiss();
                    ShowDialog("Login Failed!! Try Again..." + "\n" + task.getException());
                }
            }
        });
    }

    private void ShowDialog(String message)
    {
        final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(LoginActivity.this);

        // Setting Dialog Message
        alertDialog1.setMessage(message);

        // Setting OK Button
        alertDialog1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialog1.create();

        alertDialog.setTitle("LOGIN FAILED");

        // Showing Alert Message
        alertDialog1.show();
    }

    public void ForgotPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
    }
}