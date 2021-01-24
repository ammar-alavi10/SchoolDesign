package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.FoldingCellListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.NoticeModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {

    @BindView(R.id.notice_RecyclerView)
    protected RecyclerView mRecyclerView;

    private List<NoticeModel> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        Toolbar toolbar = findViewById(R.id.notice_toolbar);
        toolbar.setTitle("Notice");
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.notice_RecyclerView);
        items = new ArrayList<>();

        initList();

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
            }
        });
    }

    private void initList() {
        FirebaseFirestore.getInstance().collection("notice")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult() != null)
                            {
                                Log.d("Notice", task.getResult().getDocuments().toString());
                                List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                for(DocumentSnapshot documentSnapshot : myListOfDocuments)
                                {
                                    HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();
                                    Log.d("Notice", map.toString());
                                    NoticeModel noticeModel = new NoticeModel();
                                    noticeModel.setTitle((String) map.get("title"));
                                    noticeModel.setNotice((String) map.get("notice"));
                                    noticeModel.setFileUrl((String) map.get("fileUrl"));
                                    noticeModel.setDate((String) map.get("date"));
                                    noticeModel.expanded = false;
                                    items.add(noticeModel);
                                }
                            }

                        }

                        FoldingCellListAdapter adapter = new FoldingCellListAdapter(items, NoticeActivity.this, "notice");
                        mRecyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Category", " I ");
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
            SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
            int category = preferences.getInt("category", -1);
            Log.d("Category", category + " ");
            if(category == 0)
            {
                Log.d("Category", "Inside");
                getMenuInflater().inflate(R.menu.notice_menu, menu);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_notice:
                startActivity(new Intent(NoticeActivity.this, AddNotice.class));
        }
        return super.onOptionsItemSelected(item);

    }
}