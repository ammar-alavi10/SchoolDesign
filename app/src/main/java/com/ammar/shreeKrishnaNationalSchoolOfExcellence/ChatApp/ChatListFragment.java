package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.ChatListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.ChatListModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.ChatModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ChatListModel> chatListModels;
    private DatabaseReference databaseReference;
    private ChatListAdapter chatListAdapter;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        recyclerView = view.findViewById(R.id.chat_list_recycler);

        chatListModels = new ArrayList<>();
        chatListAdapter = new ChatListAdapter(getContext(), chatListModels);
        recyclerView.setAdapter(chatListAdapter);
        InitializeRecyclerView();

        return view;
    }

    private void InitializeRecyclerView() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("chatlist").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatListModels.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ChatListModel chatListModel = dataSnapshot.getValue(ChatListModel.class);
                    chatListModels.add(chatListModel);
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadChats() {
        chatListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.chat_menu_nav, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.create_group:
                startActivity(new Intent(getActivity(), CreateGroupActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}