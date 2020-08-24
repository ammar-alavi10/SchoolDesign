package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.GroupChatAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.APIService;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Client;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Data;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Response;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Sender;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Token;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.GroupChatListModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.GroupMessage;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView textView;
    private EditText messageTv;
    private ImageButton btnSend;
    private String groupTitle, myUid, groupId, myName;
    private boolean notify = false;
    private GroupChatListModel groupChatListModel;
    private GroupChatAdapter groupChatAdapter;
    private List<GroupMessage> groupMessages;
    private APIService apiService;
    List<String> participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        toolbar = findViewById(R.id.chat_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        groupMessages = new ArrayList<>();
        recyclerView = findViewById(R.id.group_chat_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageTv = findViewById(R.id.message_et);
        textView = findViewById(R.id.toolbar_group_name);
        btnSend = findViewById(R.id.sendBtn);

        groupId = getIntent().getStringExtra("groupId");
        participants = new ArrayList<>();

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        SharedPreferences preferences = getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
        int category = preferences.getInt("category", -1);

        if(category == 1)
        {
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child("teachers").child(myUid);
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel model = snapshot.getValue(UserModel.class);
                    myName = model.getName();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        if(category == 2)
        {
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child("students").child(myUid);
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel model = snapshot.getValue(UserModel.class);
                    myName = model.getName();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");
        databaseReference.child(groupId).child("groupTitle").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null) {
                    groupTitle = snapshot.getValue().toString();
                    textView.setText(groupTitle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String message = messageTv.getText().toString();
                if(TextUtils.isEmpty(message))
                {
                    messageTv.setError("Please Enter a message to send");
                    messageTv.requestFocus();
                }
                else{
                    sendMessage(message);
                }
            }
        });

        readMessages();

    }

    private void readMessages() {
        groupMessages = new ArrayList<>();
        groupChatAdapter = new GroupChatAdapter(groupMessages, GroupActivity.this);
        recyclerView.setAdapter(groupChatAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).child("messages");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists())
                {
                    GroupMessage groupMessage = snapshot.getValue(GroupMessage.class);
                    groupMessages.add(groupMessage);
                    groupChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
                /*addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    groupMessages.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        GroupMessage groupMessage = dataSnapshot.getValue(GroupMessage.class);
                        groupMessages.add(groupMessage);
                    }
                    groupChatAdapter = new GroupChatAdapter(groupMessages, GroupActivity.this);
                    recyclerView.setAdapter(groupChatAdapter);
                    groupChatAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    private void sendMessage(String message) {
        String timestamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);

        databaseReference.child(groupId).child("messages").child(timestamp).setValue(hashMap);
        messageTv.setText("");
        if(notify)
        {
            notification(message);
        }
        notify = false;
    }

    private void addParticipants(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("groups")
                .child(groupId).child("participants");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    String participantUid = dataSnapshot.child("uid").getValue(String.class);
                    if (participantUid != null && !participantUid.equals(myUid)) {
                        participants.add(participantUid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void notification(String message) {
        if(participants.size() == 0 || myName == null)
        {
            Toast.makeText(GroupActivity.this, "Please try Again", Toast.LENGTH_LONG).show();
        }
        for (String hisUid : participants)
        {
            sendNotification(hisUid, myName, message);
        }
    }

    private void sendNotification(final String hisUid, final String name, final String message) {
        DatabaseReference allTokens = FirebaseDatabase.getInstance().getReference().child("Tokens");
        Query query = allTokens.orderByKey().equalTo(hisUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Token token = dataSnapshot.getValue(Token.class);
                    Data data = new Data(myUid,  groupTitle + "/n" + name + ": " + message, "New Message", hisUid, R.drawable.ic_chat_icon, "g");

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(GroupActivity.this, response.message(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}