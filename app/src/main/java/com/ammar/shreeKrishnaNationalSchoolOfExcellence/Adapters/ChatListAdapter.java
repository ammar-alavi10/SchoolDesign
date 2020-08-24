package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.ChatActivity;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.ChatListModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<ChatListModel> chatListModels;

    public ChatListAdapter(Context context, List<ChatListModel> chatListModels) {
        this.context = context;
        this.chatListModels = chatListModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chat_list, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final String hisUid = chatListModels.get(position).getUid();
        String hisName = chatListModels.get(position).getName();
        String lastMessage = chatListModels.get(position).getMessage();

        if(lastMessage == null || lastMessage.equals("default"))
        {
            ((ChatListViewHolder) holder).message.setVisibility(View.GONE);
        }
        else {
            ((ChatListViewHolder) holder).message.setVisibility(View.VISIBLE);
            ((ChatListViewHolder) holder).message.setText(lastMessage);
        }

        ((ChatListViewHolder) holder).name.setText(hisName);

        ((ChatListViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uid", hisUid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatListModels.size();
    }

    private class ChatListViewHolder extends RecyclerView.ViewHolder {

        TextView name,message;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.chat_list_name);
            message = itemView.findViewById(R.id.chat_list_message);
        }
    }
}
