package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.GroupMessage;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GroupChatAdapter extends RecyclerView.Adapter {

    private static int Message_Right = 1;
    private static int Message_Left = 0;
    List<GroupMessage> groupMessages;
    Context context;

    FirebaseUser firebaseUser;

    public GroupChatAdapter(List<GroupMessage> groupMessages, Context context) {
        this.groupMessages = groupMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Message_Left)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.row_group_left, parent, false);
            return new GroupChatViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.row_group_right, parent, false);
            return new GroupChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        String timestamp = groupMessages.get(position).getTimestamp();
        String message = groupMessages.get(position).getMessage();

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime = DateFormat.format("dd/MM/yyyy \nhh:mm aa", calendar).toString();

        ((GroupChatViewHolder) holder).timeTv.setText(dateTime);

        ((GroupChatViewHolder) holder).messageTv.setText(message);

        if(groupMessages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
        {
            ((GroupChatViewHolder) holder).senderTv.setText("You");
        }

        else {
            SharedPreferences preferences = context.getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", Context.MODE_PRIVATE);
            int category = preferences.getInt("category", -1);

            String hisUid = groupMessages.get(position).getSender();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

            if(category == 1)
            {
                databaseReference.child("students").child(hisUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String hisName = userModel.getName();
                        ((GroupChatViewHolder) holder).senderTv.setText(hisName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            if(category == 2)
            {
                databaseReference.child("teachers").child(hisUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        String hisName = userModel.getName();
                        ((GroupChatViewHolder) holder).senderTv.setText(hisName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return groupMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser.getUid().equals(groupMessages.get(position).getSender()))
        {
            return Message_Right;
        }
        else{
            return Message_Left;
        }
    }

    private class GroupChatViewHolder extends RecyclerView.ViewHolder {

        TextView messageTv, senderTv, timeTv;

        public GroupChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTv = itemView.findViewById(R.id.message_tv);
            senderTv = itemView.findViewById(R.id.sender_tv);
            timeTv = itemView.findViewById(R.id.timeTv);

        }
    }
}
