package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.SelectParticipantsAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.UsersAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupSelectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<UserModel> users;
    private SelectParticipantsAdapter usersAdapter;
    private String class_name, groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);

        recyclerView = findViewById(R.id.users_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(GroupSelectActivity.this));

        class_name = getIntent().getStringExtra("class_name");
        groupId = getIntent().getStringExtra("groupId");


        users = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
        int category = preferences.getInt("category", -1);

        if(category == 1)
        {
            getAllStudentNames();
        }
        else if(category == 2)
        {
            getAllTeacherNames();
        }

    }

    private void getAllStudentNames() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersAdapter = new SelectParticipantsAdapter(users, GroupSelectActivity.this, groupId);
        recyclerView.setAdapter(usersAdapter);

        if(class_name != null)
        {
            Log.d("UsersList", "Generating student names 2");
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("students");
            databaseReference.orderByChild("class_name").equalTo(class_name).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("UsersList", "Generating student names 3");
                    if (snapshot.exists())
                    {
                        Log.d("UsersList", "Generating student names 4");
                        for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Log.d("UsersList", "Generating student names 5");
                            UserModel userModel = dataSnapshot.getValue(UserModel.class);

                            if(!userModel.getUid().equals(user.getUid()))
                            {
                                users.add(userModel);
                            }
                        }
                        usersAdapter.notifyDataSetChanged();
                    }
                    else{
                        Log.d("UsersList", "Generating student names 7");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("UsersList", "Generating student names 6");
                }
            });
        }

    }

    private void getAllTeacherNames() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usersAdapter = new SelectParticipantsAdapter(users, GroupSelectActivity.this, groupId);
        recyclerView.setAdapter(usersAdapter);

        if(class_name != null)
        {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child("teachers");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    users.clear();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);

                        if(!userModel.getUid().equals(user.getUid()))
                        {
                            users.add(userModel);
                        }
                    }
                    usersAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void CreateGroup(View view) {
        HashMap<String, Object> hashMap = usersAdapter.Participants;
        for(HashMap.Entry mapEl : hashMap.entrySet())
        {
            HashMap<String, String> val = (HashMap<String, String>) mapEl.getValue();
            String key = (String) mapEl.getKey();
            FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).child("participants")
                    .child(key).setValue(val).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Member Added", "Successful");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Member Not Added", e.getStackTrace().toString());
                        }
                    });
        }
        Intent intent = new Intent(GroupSelectActivity.this, GroupActivity.class);
        intent.putExtra("groupId", groupId);
        startActivity(intent);
        finish();
    }
}