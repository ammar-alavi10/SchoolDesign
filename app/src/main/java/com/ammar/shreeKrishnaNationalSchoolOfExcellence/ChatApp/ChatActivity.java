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

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.ChatAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.APIService;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Client;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Data;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Response;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Sender;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Token;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.ChatModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.UserModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView textView;
    private EditText messageTv;
    private ImageButton btnSend;
    private String hisUid, myUid, hisName;
    private ChatAdapter chatAdapter;

    private APIService apiService;
    boolean notify = false;

    private FirebaseUser firebaseUser;
    private List<ChatModel> chatModels;
    int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.chat_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.chat_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageTv = findViewById(R.id.message_et);
        textView = findViewById(R.id.toolbar_chat_name);
        btnSend = findViewById(R.id.sendBtn);

        apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

        hisUid = getIntent().getStringExtra("uid");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        SharedPreferences preferences = getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
        category = preferences.getInt("category", -1);

        if(category == 1)
        {
            databaseReference.child("students").child(hisUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    hisName = userModel.getName();
                    textView.setText(hisName);
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
                    hisName = userModel.getName();
                    textView.setText(hisName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        chatModels = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatModels, ChatActivity.this);
        recyclerView.setAdapter(chatAdapter);
        DatabaseReference databaseReference;
        if(hisUid.compareTo(myUid) > 0)
        {
            databaseReference = FirebaseDatabase.getInstance().getReference().child("chats").child(hisUid + myUid);
        }
        else{
            databaseReference = FirebaseDatabase.getInstance().getReference().child("chats").child(myUid + hisUid);
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatModels.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    Log.d("Chats", dataSnapshot.getValue().toString());
                    ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                    chatModels.add(chatModel);
                }
                chatAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(chatModels.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendMessage(final String message) {

        String timestamp = String.valueOf(System.currentTimeMillis());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("chats");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myUid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        if(hisUid.compareTo(myUid) > 0)
        {
            databaseReference.child(hisUid + myUid).push().setValue(hashMap);
        }
        else
        {
            databaseReference.child(myUid + hisUid).push().setValue(hashMap);
        }
        messageTv.setText("");

        final String[] myName = new String[1];

        if(category == 1)
        {
            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("users").child("teachers").child(myUid);
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("Messages", snapshot.getValue().toString());
                    UserModel model = snapshot.getValue(UserModel.class);
                    myName[0] = model.getName();
                    if(notify)
                    {
                        sendNotification(hisUid, model.getName(), message);
                    }
                    notify = false;
                    addMessageToList(myName[0], message);
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
                    Log.d("Messages", snapshot.getValue().toString());
                    UserModel model = snapshot.getValue(UserModel.class);
                    myName[0] = model.getName();
                    if(notify)
                    {
                        sendNotification(hisUid, model.getName(), message);
                    }
                    notify = false;
                    addMessageToList(myName[0], message);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void addMessageToList(String myName, String message) {
        final HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("uid", hisUid);
        hashMap1.put("name", hisName);
        hashMap1.put("message", message);

        final DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference().child("chatlist")
                .child(myUid).child(hisUid);
        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatRef1.setValue(hashMap1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final HashMap<String, String> hashMap2 = new HashMap<>();
        hashMap2.put("uid", myUid);
        hashMap2.put("name", myName);
        hashMap2.put("message", message);

        final DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference().child("chatlist")
                .child(hisUid).child(myUid);
        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatRef2.setValue(hashMap2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                    Data data = new Data(myUid, name + ": " + message, "New Message", hisUid, R.drawable.ic_chat_icon, "p");

                    Sender sender = new Sender(data, token.getToken());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(ChatActivity.this, response.message(), Toast.LENGTH_LONG).show();
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