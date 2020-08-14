package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.Question;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.TestModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddQuestion extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText question, option1, option2, option3, option4, correctOption;
    private TestModel testModel;
    private List<Question> questions;
    private boolean isImage = false;
    private String imageurl;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath = null;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private String subject_name, class_name, testTitle, testTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        testTitle = getIntent().getStringExtra("test_title");
        testTime = getIntent().getStringExtra("test_time");

        progressBar = findViewById(R.id.progressBar_cyclic);
        questions = new ArrayList<>();
        question = findViewById(R.id.question_et);
        option1 = findViewById(R.id.option1_et);
        option2 = findViewById(R.id.option2_et);
        option3 = findViewById(R.id.option3_et);
        option4 = findViewById(R.id.option4_et);
        correctOption = findViewById(R.id.correct_et);
        testModel = new TestModel();

        storageReference = FirebaseStorage.getInstance().getReference("TestImages");
        db = FirebaseFirestore.getInstance();
    }

    public void AddImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
        }
    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void Add(View view) {
        progressBar.setVisibility(View.VISIBLE);
        String ques = question.getText().toString();
        if(TextUtils.isEmpty(ques)) {
            question.requestFocus();
            question.setError("Enter a question");
            return;
        }

        String op1 = option1.getText().toString();
        if(TextUtils.isEmpty(op1)) {
            option1.requestFocus();
            option1.setError("Enter two options");
            return;
        }

        String op2 = option2.getText().toString();
        if(TextUtils.isEmpty(op2)) {
            option2.requestFocus();
            option2.setError("Enter two options");
            return;
        }

        String op3 = option3.getText().toString();
        String op4 = option4.getText().toString();
        String correct = correctOption.getText().toString();
        if(TextUtils.isEmpty(correct)) {
            correctOption.requestFocus();
            correctOption.setError("Enter a correct option");
            return;
        }

        final Question question1 = new Question();
        question1.setQuestion(ques);
        question1.setOption1(op1);
        question1.setOption2(op2);
        question1.setOption3(op3);
        question1.setOption4(op4);
        question1.setCorrect(Integer.parseInt(correct));
        if(filePath != null)
        {
            isImage = true;
            final StorageReference ref = storageReference.child(System.currentTimeMillis() + "." + getExt(filePath));
            UploadTask uploadTask = ref.putFile(filePath);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw Objects.requireNonNull(task.getException());
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful() && task.getResult() != null)
                    {
                        imageurl = task.getResult().toString();
                        question1.setImage(isImage);
                        question1.setImageurl(imageurl);
                        questions.add(question1);
                        Toast.makeText(AddQuestion.this, "Question Added", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(AddQuestion.this, "Image Upload Failed... Try again", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        else{
            question1.setImage(isImage);
            question1.setImageurl(null);
            questions.add(question1);
            Toast.makeText(AddQuestion.this, "Question Added", Toast.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.GONE);
    }

    public void UploadTest(View view) {
        progressBar.setVisibility(View.VISIBLE);
        testModel = new TestModel();
        testModel.setSubject_name(subject_name + class_name);
        testModel.setTestTitle(testTitle);
        testModel.setQuestions(questions);
        testModel.setNo_of_ques(questions.size());
        testModel.setTestTime(testTime);
        db.collection("test").document(subject_name + class_name + testTitle).
            set(testModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(AddQuestion.this, "Test Uploaded", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(AddQuestion.this, "Problem in Uploading.. Please Try Again", Toast.LENGTH_LONG).show();
                }
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    public void AddNewQues(View view) {
        question.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
        correctOption.setText("");
        isImage = false;
        imageurl = null;
    }
}