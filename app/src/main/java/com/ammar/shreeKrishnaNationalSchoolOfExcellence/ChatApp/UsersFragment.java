package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.UsersAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<UserModel> users;
    private UsersAdapter usersAdapter;
    private String class_name;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.users_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            class_name = bundle.getString("class_name", null);
        }

        users = new ArrayList<>();

        SharedPreferences preferences = this.getActivity().getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
        int category = preferences.getInt("category", -1);

        if(category == 1)
        {
            Log.d("UsersList", "Generating student names");
            getAllStudentNames();
        }
        else if(category == 2)
        {
            Log.d("UsersList", "Generating teacher names");
            getAllTeacherNames();
        }

        return view;
    }

    private void getAllStudentNames() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

                            usersAdapter = new UsersAdapter(users, getActivity());
                            recyclerView.setAdapter(usersAdapter);
                        }
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

                        usersAdapter = new UsersAdapter(users, getActivity());
                        recyclerView.setAdapter(usersAdapter);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}