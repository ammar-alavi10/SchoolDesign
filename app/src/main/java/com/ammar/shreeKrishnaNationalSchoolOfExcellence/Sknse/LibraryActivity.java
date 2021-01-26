package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Adapters.NotesListAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LibraryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    EditText class_et;
    private List<String> notesTitle, notesUrl;
    String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        Toolbar toolbar = findViewById(R.id.library_toolbar);
        toolbar.setTitle("Library");
        setSupportActionBar(toolbar);

        notesTitle = new ArrayList<>();
        notesUrl = new ArrayList<>();

        class_et = findViewById(R.id.class_et);

        recyclerView = findViewById(R.id.recyclerview_ShowNotesList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
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
        if (item.getItemId() == R.id.add_notice) {
            startActivity(new Intent(LibraryActivity.this, AddBook.class));
        }
        return super.onOptionsItemSelected(item);

    }

    public void ShowBooks(View view) {
        class_name = class_et.getText().toString();
        InstantiateRecyclerView();
    }

    private void InstantiateRecyclerView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("library")
                .whereEqualTo("subject", class_name)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Log.d("QueryResult", document.getId() + " => " + document.getData());
                            HashMap<String, String> videoModel = (HashMap) document.getData();
                            notesTitle.add(videoModel.get("name"));
                            notesUrl.add(videoModel.get("notesurl"));
                        }
                    }
                    setAdapter();
                } else {
                    Toast.makeText(getApplicationContext(), "Loading Failed!! Try Again...", Toast.LENGTH_LONG).show();
                    Log.d("Category", "get failed with ", task.getException());
                }
            }
        });

    }

    private void setAdapter() {
        NotesListAdapter.RecyclerNotesViewClickListener listener = new NotesListAdapter.RecyclerNotesViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), ShowNotes.class);
                intent.putExtra("notestitle", notesTitle.get(position));
                intent.putExtra("notesurl", notesUrl.get(position));
                startActivity(intent);
            }

            @Override
            public void onLongClick(View v, final int position) {
                SharedPreferences preferences = getSharedPreferences("com.ammar.shreeKrishnaNationalSchoolOfExcellence", MODE_PRIVATE);
                int category = preferences.getInt("category", -1);
                if(category == 1)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LibraryActivity.this);
                    builder.setTitle("Confirm Delete !");
                    builder.setMessage("You are about to delete this book. Do you really want to proceed ?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("library").whereEqualTo("name", notesTitle.get(position)).whereEqualTo("subject", class_name).
                                    get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                    {
                                        if (task.getResult() != null) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String id = document.getId();
                                                db.collection("library").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            InstantiateRecyclerView();
                                                            Toast.makeText(getApplicationContext(), "Book Deleted", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            }
        };
        recyclerView.setAdapter(new NotesListAdapter(notesTitle, notesUrl, listener));
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(LibraryActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}