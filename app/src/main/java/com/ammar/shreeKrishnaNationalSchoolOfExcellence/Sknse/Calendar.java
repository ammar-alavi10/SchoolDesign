package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Calendar extends AppCompatActivity {

    int category;
    String class_name;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        GetUrl();
    }

    public void GetUrl()
    {
        FirebaseFirestore.getInstance().collection("calendar").document("calendar").get()
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

    public void HomeClicked(View view) {
        Intent intent = new Intent(Calendar.this, StartActivity.class);
        startActivity(intent);
        finish();
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

        }
        return super.onOptionsItemSelected(item);
    }
}