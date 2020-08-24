package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.GroupActivity;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.GroupChatListModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<GroupChatListModel> groupChatListModels;

    public GroupListAdapter(Context context, List<GroupChatListModel> groupChatListModels) {
        this.context = context;
        this.groupChatListModels = groupChatListModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_groups, parent, false);
        return new GroupListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final String title = groupChatListModels.get(position).getGroupTitle();
        final String groupId = groupChatListModels.get(position).getGroupId();
        Log.d("groupList", title + " , " +groupId);
        ((GroupListViewHolder) holder).groupName.setText(title);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups")
                .child(groupId).child("messages");
        databaseReference.limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        String message = (String) dataSnapshot.child("message").getValue();
                        String senderUid = (String) dataSnapshot.child("sender").getValue();
                        ((GroupListViewHolder) holder).message.setText(message);

                        setName(holder, position, senderUid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ((GroupListViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GroupActivity.class);
                intent.putExtra("groupId", groupId);
                context.startActivity(intent);
            }
        });
    }

    private void setName(final RecyclerView.ViewHolder holder, int position, final String senderUid) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

            databaseReference.child("students").child(senderUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue() != null) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String hisName = userModel.getName();
                        ((GroupListViewHolder) holder).sender.setText(hisName);
                    }
                    else{
                        databaseReference.child("teachers").child(senderUid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                UserModel userModel = snapshot.getValue(UserModel.class);
                                String hisName = userModel.getName();
                                ((GroupListViewHolder) holder).sender.setText(hisName);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
    }

    @Override
    public int getItemCount() {
        return groupChatListModels.size();
    }

    private class GroupListViewHolder extends RecyclerView.ViewHolder{

        TextView groupName, sender, message;

        public GroupListViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.group_name);
            sender = itemView.findViewById(R.id.sender_name);
            message = itemView.findViewById(R.id.message_last);
        }
    }
}
