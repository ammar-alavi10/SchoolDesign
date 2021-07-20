package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.Question;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.TestModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.TestResultModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private EditText correct_et;
    private TextView question, timer, correct, wrongAns, unanswered, question_no;
    private RadioButton option1, option2, option3, option4;
    private RadioGroup radioGroup;
    private int score, wrong;
    private LinearLayout test, result;
    private ProgressBar progressBar;
    private String class_name, subject_name, chapter_no, test_title, testType;
    private Button show, next;
    private String imageUrl;
    private int selectedOption, correctOption;
    private String correctAns, typedAns;
    String sharedPrefFile = "com.ammar.shreeKrishnaNationalSchoolOfExcellence";
    SharedPreferences preferences;
    private TestModel testModel;
    private int questionNo;
    private long endTime, mTimeLeftInMillis;
    private Question questionModel;
    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        correct_et = findViewById(R.id.correct_ans_et);
        question_no = findViewById(R.id.question_no);
        preferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        questionNo = preferences.getInt("question_no", 0);
        score = preferences.getInt("test_score", 0);
        wrong = preferences.getInt("wrong", 0);
        endTime = preferences.getLong("end_time", -1);

        test = findViewById(R.id.layout_test);
        result = findViewById(R.id.layout_result);

        correct = findViewById(R.id.correct_ans);
        wrongAns = findViewById(R.id.wrong_ans);
        unanswered = findViewById(R.id.unanswered_ans);


        progressBar = findViewById(R.id.my_progressBar);
        timer = findViewById(R.id.timer);
        question = findViewById(R.id.question);
        radioGroup = findViewById(R.id.radio_group);
        option1 = findViewById(R.id.rb1);
        option2 = findViewById(R.id.rb2);
        option3 = findViewById(R.id.rb3);
        option4 = findViewById(R.id.rb4);
        show = findViewById(R.id.show);
        next = findViewById(R.id.next_ques);

        db = FirebaseFirestore.getInstance();

        testType = getIntent().getStringExtra("testType");
        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        chapter_no = getIntent().getStringExtra("chapter_no");
        test_title = getIntent().getStringExtra("test_title");

        instantiateTest();
    }

    private void instantiateTest() {
        db.collection("test").document(chapter_no + subject_name + class_name + test_title + testType)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null && documentSnapshot.exists())
                    {
                        if(documentSnapshot.getData() != null)
                        {
                            Map<String, Object> test = documentSnapshot.getData();
                            testModel = new TestModel();
                            testModel.setTestTitle((String) test.get("testTitle"));
                            testModel.setTestTime((String) test.get("testTime"));
                            testModel.setNo_of_ques(((Long) test.get("no_of_ques")).intValue());
                            testModel.setSubject_name((String) test.get("subject_name"));

                            List<Question> q = new ArrayList<>();
                            List<Map<String, Object>> list_of_questions = (List<Map<String, Object>>) test.get("questions");
                            for(Map<String, Object> lo: list_of_questions)
                            {
                                Question qs = new Question();
                                qs.setQuestion((String) lo.get("question"));
                                qs.setImageurl((String) lo.get("imageurl"));
                                qs.setImage((boolean) lo.get("image"));
                                if(testType.equals("blanks"))
                                {
                                    qs.setCorrectAns((String) lo.get("correctAns"));
                                }
                                else {
                                    qs.setCorrect(((Long) lo.get("correct")).intValue());
                                    qs.setOption1((String) lo.get("option1"));
                                    qs.setOption2((String) lo.get("option2"));
                                    qs.setOption3((String) lo.get("option3"));
                                    qs.setOption4((String) lo.get("option4"));
                                }
                                q.add(qs);
                            }

                            testModel.setQuestions(q);
                            progressBar.setMax(testModel.getNo_of_ques());
                            if(endTime == -1)
                            {
                                mTimeLeftInMillis = (long) Integer.parseInt(testModel.getTestTime()) * 60 * 1000;
                                Date curDate = new Date();
                                long curMillis = curDate.getTime();
                                endTime = curMillis + mTimeLeftInMillis;
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("end_time", endTime);
                                editor.apply();
                            }
                            else{
                                Date curDate = new Date();
                                long curMillis = curDate.getTime();
                                mTimeLeftInMillis = endTime - curMillis;
                            }
                            if(mTimeLeftInMillis < 0)
                            {
                                finishTest();
                            }
                            else
                            {
                                startTimer();
                                prepareQues();
                            }
                        }
                        else{
                            Toast.makeText(TestActivity.this, "Error in Loading Test", Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(TestActivity.this, "Error in Loading Test", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(TestActivity.this, "Error in Loading Test", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("test_score", score);
        editor.putInt("question_no", questionNo);
        editor.putInt("wrong", wrong);
        editor.putLong("end_time", endTime);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(endTime != -1)
        {
            endTime = preferences.getLong("end_time", -1);
            Date date = new Date();
            long currMillis = date.getTime();
            mTimeLeftInMillis = endTime - currMillis;
            if(mTimeLeftInMillis < 0)
            {
                finishTest();
            }
            else {
                startTimer();
            }
        }
    }

    private void prepareQues() {
        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Loading Question");
        progressDialog.show();
        progressBar.setProgress(questionNo + 1);
        String quesno = (questionNo + 1) + "/" + testModel.getNo_of_ques();
        question_no.setText(quesno);
        selectedOption = 0;
        typedAns = "";
        if(questionNo == testModel.getNo_of_ques() - 1)
        {
            next.setText("Finish");
        }
        questionModel = testModel.getQuestions().get(questionNo);
        if(questionModel.isImage())
        {
            show.setVisibility(View.VISIBLE);
            imageUrl = questionModel.getImageurl();
        }
        else{
            show.setVisibility(View.GONE);
            imageUrl = null;
        }
        String ques = "Q. " + questionModel.getQuestion();
        question.setText(ques);
        if(testType.equals("blanks"))
        {
            correct_et.setVisibility(View.VISIBLE);
            correctAns = questionModel.getCorrectAns();
        }
        else{
            radioGroup.setVisibility(View.VISIBLE);
            option1.setText(questionModel.getOption1());
            option2.setText(questionModel.getOption2());
            if (!questionModel.getOption3().equals("")) {
                option3.setText(questionModel.getOption3());
            } else {
                option3.setVisibility(View.GONE);
            }
            if (!questionModel.getOption4().equals("")) {
                option4.setText(questionModel.getOption4());
            } else {
                option4.setVisibility(View.GONE);
            }
            correctOption = questionModel.getCorrect();
        }
        progressDialog.dismiss();

    }

    public void ShowPopUp(View view) {
        Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.initiatePopupWithPicasso(imageUrl);
        imagePopup.viewPopup();
    }

    public void NextQues(View view) {

        if(testType.equals("blanks"))
        {
            typedAns = correct_et.getText().toString().toLowerCase().trim();
            if(typedAns.equals(correctAns))
            {
                score++;
            }
            else{
                wrong++;
            }
        }
        else {
            int radioButtonID = radioGroup.getCheckedRadioButtonId();
            if(radioButtonID != -1)
            {
                View radioButton = radioGroup.findViewById(radioButtonID);
                selectedOption = radioGroup.indexOfChild(radioButton);

                if (selectedOption == correctOption - 1) {
                    score++;
                } else {
                    wrong++;
                }
            }
        }

        if(questionNo == testModel.getNo_of_ques() - 1)
        {
            finishTest();
        }
        else{
            questionNo = questionNo + 1;
            prepareQues();
        }
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                finishTest();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeLeftFormatted);
    }

    private void finishTest() {

        test.setVisibility(View.GONE);
        result.setVisibility(View.VISIBLE);

        String text = "Correct Answers : " + score;
        correct.setText(text);

        text = "Wrong Answer : " + wrong;
        wrongAns.setText(text);

        text = "Unanswered : " + (testModel.getNo_of_ques() - (score + wrong));
        unanswered.setText(text);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = "";
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        TestResultModel testResultModel = new TestResultModel();
        testResultModel.setSubject_name(chapter_no + subject_name + class_name + uid);
        testResultModel.setSearchName(subject_name + class_name + uid);
        testResultModel.setCorrect(score);
        testResultModel.setWrong(wrong);
        testResultModel.setUnanswered(testModel.getNo_of_ques() - (score + wrong));
        testResultModel.setTitle(test_title);
        testResultModel.setTotal(testModel.getNo_of_ques());
        testResultModel.setUid(uid);
        db.collection("testscores").document(chapter_no + subject_name + class_name + test_title + uid)
                .set(testResultModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(TestActivity.this, "Test Ended", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(TestActivity.this, "Problem in Uploading Test Result", Toast.LENGTH_LONG).show();
                }
            }
        });

        questionNo = 0;
        score = 0;
        wrong = 0;
        endTime = -1;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("test_score", 0);
        editor.putInt("question_no", 0);
        editor.putInt("wrong", 0);
        editor.putLong("end_time", -1);
        editor.apply();

    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(TestActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    public void ClearSelection(View view) {
        radioGroup.clearCheck();
    }
}