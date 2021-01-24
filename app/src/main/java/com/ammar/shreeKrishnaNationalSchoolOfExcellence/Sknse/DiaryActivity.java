package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.FoldingCellListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.NoticeModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiaryActivity extends AppCompatActivity {

    @BindView(R.id.notice_RecyclerView)
    protected RecyclerView mRecyclerView;
    private String class_name;
    private TextView textView;

    private List<NoticeModel> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Toolbar toolbar = findViewById(R.id.notice_toolbar);
        toolbar.setTitle("Class Diary");
        setSupportActionBar(toolbar);

        textView = findViewById(R.id.no_class);

        FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null)
                    {
                        textView.setVisibility(View.GONE);
                        class_name = documentSnapshot.getString("class");
                        initList();
                    }
                    else
                    {
                        textView.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });

        mRecyclerView = findViewById(R.id.notice_RecyclerView);
        items = new ArrayList<>();

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
        FirebaseFirestore.getInstance().collection("diary" + class_name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult() != null)
                            {
                                List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                for(DocumentSnapshot documentSnapshot : myListOfDocuments)
                                {
                                    HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();
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

                        FoldingCellListAdapter adapter = new FoldingCellListAdapter(items, DiaryActivity.this, "diary" + class_name);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
            SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
            int category = preferences.getInt("category", -1);
            if(category == 1)
            {
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
                startActivity(new Intent(DiaryActivity.this, AddClassDiary.class));
        }
        return super.onOptionsItemSelected(item);

    }
}