package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowNotes extends AppCompatActivity {

    private String name, url;
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notes);

        name = getIntent().getStringExtra("notestitle");
        url = getIntent().getStringExtra("notesurl");

        pdfView = findViewById(R.id.pdf_view);
        new RetrievePdfStream().execute(url);
    }

    class RetrievePdfStream extends AsyncTask<String, Void, InputStream>{

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
}