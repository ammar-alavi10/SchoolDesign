package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class AttendanceUpload extends AppCompatActivity {

    EditText class_et, date_et;
    String class_name, date_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_upload);

        class_et = findViewById(R.id.class_et);
        date_et = findViewById(R.id.date_et);

    }

    public void ChooseExcelFile(View view) {
        Intent intent = new Intent();
        intent.setType("application/vnd.ms-excel");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void Upload(View view) {
    }
}