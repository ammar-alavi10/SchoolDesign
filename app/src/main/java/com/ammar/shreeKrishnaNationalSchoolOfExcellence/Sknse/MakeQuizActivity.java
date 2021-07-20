package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class MakeQuizActivity extends AppCompatActivity {

    String subject_name, class_name, testType, chapter_no;
    EditText testTitle, testTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_quiz);

        testType = getIntent().getStringExtra("type");

        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        chapter_no = getIntent().getStringExtra("chapter_no");

        testTime = findViewById(R.id.testtime);
        testTitle = findViewById(R.id.testtitle);
    }

    public void AddQuestions(View view) {
        String title = testTitle.getText().toString();
        if(TextUtils.isEmpty(title)) {
            testTitle.requestFocus();
            testTitle.setError("Enter a title for the test");
            return;
        }
        String time = testTime.getText().toString();
        if(TextUtils.isEmpty(time)) {
            testTime.requestFocus();
            testTime.setError("Enter the time limit for the test");
            return;
        }
        Intent intent;
        if(testType.equals("blanks"))
        {
            intent = new Intent(MakeQuizActivity.this, AddBlanksQuestion.class);
        }
        else{
            intent = new Intent(MakeQuizActivity.this, AddQuestion.class);
        }
        intent.putExtra("test_title", title);
        intent.putExtra("test_time", time);
        intent.putExtra("subject_name", subject_name);
        intent.putExtra("class_name", class_name);
        intent.putExtra("chapter_no", chapter_no);
        intent.putExtra("testType", testType);
        startActivity(intent);
        finish();
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(MakeQuizActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}