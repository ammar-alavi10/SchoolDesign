package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class TimeTable extends AppCompatActivity {

    int category;
    String class_name;
    private PDFView pdfView;
    Uri pdfFile;
    private EditText editText;
    private Button button;
    private StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        editText = findViewById(R.id.class_et);
        button = findViewById(R.id.btn);

        Toolbar toolbar = findViewById(R.id.notice_toolbar);
        toolbar.setTitle("Time Table");
        setSupportActionBar(toolbar);

        storage = FirebaseStorage.getInstance().getReference("timetable");

        String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
        SharedPreferences preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        category = preferences.getInt("category", -1);
        if(category == 2)
        {
            class_name = preferences.getString("class_name", null);
            GetUrl();
        }
        else if(category == 1)
        {
            editText.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    class_name = editText.getText().toString();
                    GetUrl();
                }
            });
        }

    }

    public void GetUrl()
    {
        FirebaseFirestore.getInstance().collection("timetable").document(class_name).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null)
                            {
                                String url = documentSnapshot.getString("url");
                                ShowNote(url);
                            }
                        }
                    }
                });
    }

    private void ShowNote(String url) {
        pdfView = findViewById(R.id.pdf_view);
        new RetrievePdfStream().execute(url);
    }

    class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                if(httpURLConnection.getResponseCode() == 200)
                {
                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("Category", " I ");
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Log.d("Category", category + " ");
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
                class_name = editText.getText().toString();
                if(!Objects.equals(class_name, ""))
                {
                    Upload();
                }
                else{
                    editText.setError("enter class");
                    editText.requestFocus();
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void Upload() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            pdfFile = data.getData();
            UploadNote();
        }
    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public class TimeTableModel{
        String class_name;
        String url;

        public TimeTableModel(String class_name, String url) {
            this.class_name = class_name;
            this.url = url;
        }

        public TimeTableModel() {
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public void UploadNote() {
        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        if(pdfFile != null)
        {
            final StorageReference reference = storage.child(System.currentTimeMillis() + "." + getExt(pdfFile));
            UploadTask uploadTask = reference.putFile(pdfFile);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        TimeTableModel timeTableModel = new TimeTableModel();
                        timeTableModel.setUrl(downloadUrl != null ? downloadUrl.toString() : null);
                        timeTableModel.setClass_name(class_name);
                        FirebaseFirestore.getInstance().collection("timetable").document(class_name)
                                .set(timeTableModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(TimeTable.this, "Data saved", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(TimeTable.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(TimeTable.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(TimeTable.this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }
    }
}