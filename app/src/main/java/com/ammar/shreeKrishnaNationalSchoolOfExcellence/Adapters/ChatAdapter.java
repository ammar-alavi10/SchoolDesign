package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.ChatModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter {

    private static int Message_Right = 1;
    private static int Message_Left = 0;
    List<ChatModel> chatModels;
    Context context;

    FirebaseUser firebaseUser;

    public ChatAdapter(List<ChatModel> chatModels, Context context) {
        this.chatModels = chatModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == Message_Left)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new ChatViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new ChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ChatViewHolder) holder).messageTv.setText(chatModels.get(position).getMessage());

        String timestamp = chatModels.get(position).getTimestamp();

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime = DateFormat.format("dd/MM/yyyy \nhh:mm aa", calendar).toString();

        ((ChatViewHolder) holder).timeTv.setText(dateTime);
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser.getUid().equals(chatModels.get(position).getSender()))
        {
            return Message_Right;
        }
        else{
            return Message_Left;
        }
    }

    private class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView messageTv, timeTv;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTv = itemView.findViewById(R.id.message_tv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }
}
