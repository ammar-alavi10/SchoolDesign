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
import android.widget.Button;
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

public class AddNotice extends AppCompatActivity {

    private EditText title_et, notice_et;
    private Uri noticeFile;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private NoticeModel noticeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        title_et = findViewById(R.id.notice_title);
        notice_et = findViewById(R.id.notice_text);

        noticeModel = new NoticeModel();

        storageReference = FirebaseStorage.getInstance().getReference("Notice");
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
        Log.d("Notice", "1");
        final String title = title_et.getText().toString();
        final String notice = notice_et.getText().toString();
        if(TextUtils.isEmpty(title))
        {
            title_et.requestFocus();
            title_et.setError("Title should not be empty");
        }
        if(TextUtils.isEmpty(notice))
        {
            notice_et.requestFocus();
            notice_et.setError("Notice should not be empty");
        }
        if(noticeFile != null)
        {
            Log.d("Notice", "2");
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
                        Log.d("Notice", "3");
                        Uri downloadUrl = task.getResult();
                        noticeModel.setFileUrl(downloadUrl != null ? downloadUrl.toString() : null);
                        noticeModel.setNotice(notice);
                        noticeModel.setTitle(title);
                        noticeModel.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                        Log.d("Notice", noticeModel.toString());
                        db.collection("notice").add(noticeModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(AddNotice.this, "Data saved", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(AddNotice.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(AddNotice.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Log.d("Notice", "3");
            noticeModel.setFileUrl(null);
            noticeModel.setNotice(notice);
            noticeModel.setTitle(title);
            noticeModel.setDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
            Log.d("Notice", noticeModel.toString());
            db.collection("notice").add(noticeModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AddNotice.this, "Data saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddNotice.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}