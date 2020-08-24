package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.ChatActivity;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SelectParticipantsAdapter extends RecyclerView.Adapter {
    public HashMap<String, Object> Participants;
    private Context context;
    private List<UserModel> userModels;
    String groupId;

    public SelectParticipantsAdapter(List<UserModel> userModels, Context context, String groupId) {
        this.userModels = userModels;
        this.context = context;
        this.groupId = groupId;
        Participants = new HashMap<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.users_select, parent, false);
        return new ParticipantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        Log.d("UserAdapter", userModels.get(position).getName());
        ((ParticipantsViewHolder) holder).nameTv.setText(userModels.get(position).getName());

        ((ParticipantsViewHolder) holder).checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((ParticipantsViewHolder) holder).checkBox.isChecked();
                UserModel user = userModels.get(position);
                if(isChecked)
                {
                    final HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put("uid", user.getUid());
                    hashMap1.put("role", "member");
                    hashMap1.put("timestamp", "" + System.currentTimeMillis());
                    Participants.put(user.getUid(), hashMap1);
                    Log.d("Participants", Participants.toString());
                }
                else {
                    Participants.remove(user.getUid());
                    Log.d("Participants", Participants.toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    private class ParticipantsViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        CheckBox checkBox;

        public ParticipantsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
