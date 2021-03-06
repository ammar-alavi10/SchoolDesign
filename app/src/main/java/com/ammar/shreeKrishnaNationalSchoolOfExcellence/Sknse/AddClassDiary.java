package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.NoticeModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AddClassDiary extends AppCompatActivity {

    private EditText title_et, notice_et, class_et;
    private Uri noticeFile;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private NoticeModel noticeModel;
    private String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class_diary);

        title_et = findViewById(R.id.notice_title);
        notice_et = findViewById(R.id.notice_text);
        class_et = findViewById(R.id.class_et);

        noticeModel = new NoticeModel();

        db = FirebaseFirestore.getInstance();
    }

    public void FileChoose(View view) {
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
            noticeFile = data.getData();
        }
    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void UploadNotice(View view) {
        final String title = title_et.getText().toString();
        final String notice = notice_et.getText().toString();
        final String class_name = class_et.getText().toString();
        storageReference = FirebaseStorage.getInstance().getReference("diary" + class_name);
        if(TextUtils.isEmpty(title))
        {
            title_et.requestFocus();
            title_et.setError("Title should not be empty");
            return;
        }
        if(TextUtils.isEmpty(notice))
        {
            notice_et.requestFocus();
            notice_et.setError("Notice should not be empty");
            return;
        }
        if(TextUtils.isEmpty(class_name))
        {
            class_et.requestFocus();
            class_et.setError("Class name should not be empty");
            return;
        }
        if(noticeFile != null)
        {
            final StorageReference reference = storageReference.child(title + "." + getExt(noticeFile));
            UploadTask uploadTask = reference.putFile(noticeFile);
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
                        noticeModel.setFileUrl(downloadUrl != null ? downloadUrl.toString() : null);
                        noticeModel.setNotice(notice);
                        noticeModel.setTitle(title);
                        noticeModel.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                        db.collection("diary" + class_name).add(noticeModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(AddClassDiary.this, "Data saved", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(AddClassDiary.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(AddClassDiary.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            noticeModel.setFileUrl(null);
            noticeModel.setNotice(notice);
            noticeModel.setTitle(title);
            noticeModel.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
            db.collection("diary" + class_name).add(noticeModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AddClassDiary.this, "Data saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddClassDiary.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}