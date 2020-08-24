package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.HashMap;

public class CreateGroupActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private EditText groupTitle, groupDesc;
    private FloatingActionButton btnCreate;
    private LovelyProgressDialog progressDialog;
    private String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Toolbar toolbar = findViewById(R.id.tool);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        class_name = getIntent().getStringExtra("class_name");

        groupTitle = findViewById(R.id.group_title_et);
        groupDesc = findViewById(R.id.group_desc_et);
        btnCreate = findViewById(R.id.createGroupBtn);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateGroup();
            }
        });
    }

    private void startCreateGroup() {
        progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Creating Group");

        String title = groupTitle.getText().toString();
        String desc = groupDesc.getText().toString();

        if(TextUtils.isEmpty(title))
        {
            groupTitle.setError("Enter Group Title");
            groupTitle.requestFocus();
            return;
        }

        progressDialog.show();

        final String groupid = FirebaseAuth.getInstance().getCurrentUser().getUid() + title;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("createdAt", "" + System.currentTimeMillis());
        hashMap.put("groupId",  groupid);
        hashMap.put("groupTitle", title);
        hashMap.put("groupDesc", desc);

        final HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap1.put("role", "creator");
        hashMap1.put("timestamp", "" + System.currentTimeMillis());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");
        databaseReference.child(groupid).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                FirebaseDatabase.getInstance().getReference().child("groups").child(groupid).child("participants")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap1);
                progressDialog.dismiss();
                Intent intent = new Intent(CreateGroupActivity.this, GroupSelectActivity.class);
                intent.putExtra("groupId", groupid);
                intent.putExtra("class_name", class_name);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(CreateGroupActivity.this, "Error in creating Group", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}