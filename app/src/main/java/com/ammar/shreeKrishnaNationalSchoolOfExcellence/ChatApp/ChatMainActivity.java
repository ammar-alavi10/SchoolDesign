package com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.ChatApp.Notifications.Token;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class ChatMainActivity extends AppCompatActivity {

    ActionBar actionBar;
    BottomNavigationView bottomNavigationView;
    String class_name;
    String mUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle("");

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(itemSelectedListener);

        mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences preferences = getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("current_uid", mUid);
        editor.apply();

        class_name = getIntent().getStringExtra("class_name");

        actionBar.setTitle("Chat");
        ChatListFragment chatListFragment = new ChatListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("class_name", class_name);
        chatListFragment.setArguments(bundle);
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.frame_chat, chatListFragment, "");
        ft1.commit();

        updateToken(FirebaseInstanceId.getInstance().getToken());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu_nav, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.create_group)
        {
            Intent intent = new Intent(ChatMainActivity.this, CreateGroupActivity.class);
            intent.putExtra("class_name", class_name);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateToken(String token)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken = new Token(token);
        databaseReference.child(mUid).setValue(mToken);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener itemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId())
                    {
                        case R.id.chat_home:
                            actionBar.setTitle("Chat");
                            ChatListFragment chatListFragment = new ChatListFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("class_name", class_name);
                            chatListFragment.setArguments(bundle);
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.frame_chat, chatListFragment, "");
                            ft1.commit();
                            return true;
                        case R.id.chat_users:
                            actionBar.setTitle("Users");
                            UsersFragment usersFragment = new UsersFragment();
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("class_name", class_name);
                            usersFragment.setArguments(bundle2);
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.frame_chat, usersFragment, "");
                            ft2.commit();
                            return true;
                        case R.id.groups:
                            actionBar.setTitle("Groups");
                            GroupFragment groupFragment = new GroupFragment();
                            Bundle bundle3 = new Bundle();
                            bundle3.putString("class_name", class_name);
                            groupFragment.setArguments(bundle3);
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.frame_chat, groupFragment, "");
                            ft3.commit();
                            return true;
                    }
                    return false;
                }
            };
}