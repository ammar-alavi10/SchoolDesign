package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.GroupListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Token;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.GroupChatListModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<GroupChatListModel> groupChatListModels;
    private GroupListAdapter groupListAdapter;
    private String mUid;

    public GroupFragment() {
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
        View view = inflater.inflate(R.layout.fragment_group, container, false);

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = view.findViewById(R.id.groups_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupChatListModels = new ArrayList<>();

        updateToken(FirebaseInstanceId.getInstance().getToken());

        loadGroupChatsList();

        return view;
    }

    public void updateToken(String token)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        databaseReference.child(mUid).setValue(mToken);
    }

    private void loadGroupChatsList() {
        groupChatListModels = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupChatListModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(dataSnapshot.child("participants").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()){
                        Log.d("GroupList", dataSnapshot.getValue().toString());
                        GroupChatListModel model = dataSnapshot.getValue(GroupChatListModel.class);
                        groupChatListModels.add(model);
                    }
                }
                groupListAdapter = new GroupListAdapter(getContext(), groupChatListModels);
                recyclerView.setAdapter(groupListAdapter);
                groupListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}