package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
                            startActivity(new Intent(LoginActivity.this, SelectSubjectActivity.class));
                            finish();
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
        String username = userIdText.getText().toString();
        String password = passwordText.getText().toString();
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
                                            if(category == 2)
                                            {
                                                String class_name = (String) document.get("class");
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("class_name", class_name);
                                                editor.apply();
                                            }
                                            startActivity(new Intent(LoginActivity.this, SelectSubjectActivity.class));
                                            finish();
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
                }
                else{
                    Toast.makeText(getApplicationContext(), "Login Failed!! Try Again...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}