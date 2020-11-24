package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.VideoModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class UploadVideoActivity extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;
    VideoView videoView;
    Button button;
    ProgressBar progressBar;
    EditText editTextName;
    EditText editTextLink;
    private Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    FirebaseFirestore db;
    VideoModel member;
    UploadTask uploadTask;
    String subject, class_name, chapter_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        subject = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");
        chapter_no = getIntent().getStringExtra("chapter_no");

        member = new VideoModel();
        storageReference = FirebaseStorage.getInstance().getReference("Video");
        db = FirebaseFirestore.getInstance();



        videoView = findViewById(R.id.videoview_upload);
        button = findViewById(R.id.button_upload_video);
        progressBar = findViewById(R.id.progressBar_main);
        editTextName = findViewById(R.id.et_video_name);
        editTextLink = findViewById(R.id.youtube_link_et);
        mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadVideo();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO && resultCode == RESULT_OK &&
                data != null && data.getData() != null ){
            videoUri = data.getData();
            videoView.setVideoURI(videoUri);
        }

    }

    public void ChooseVideo(View view) {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_VIDEO);

    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void ShowVideo(View view) {

        Intent intent = new Intent(UploadVideoActivity.this, ShowVideoList.class);
        intent.putExtra("subject_name", subject);
        intent.putExtra("class_name", class_name);
        intent.putExtra("chapter_no", chapter_no);
        startActivity(intent);

    }
    private void UploadVideo(){
        final String videoName = editTextName.getText().toString();
        final String search = editTextName.getText().toString().toLowerCase();
        if (videoUri != null || !TextUtils.isEmpty(videoName)){

            progressBar.setVisibility(View.VISIBLE);
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getExt(videoUri));
            uploadTask = reference.putFile(videoUri);

            Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                progressBar.setVisibility(View.INVISIBLE);

                                member.setName(videoName);
                                member.setVideourl(downloadUrl != null ? downloadUrl.toString() : null);
                                member.setSearch(search);
                                member.setType("firebase");
                                member.setSubject(subject + class_name + chapter_no);
                                db.collection("videos").add(member).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(UploadVideoActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(UploadVideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }else {
                                Toast.makeText(UploadVideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else {
            Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT).show();
        }

    }

    public void OnLinkUploadPressed(View view) {
        final String videoName = editTextName.getText().toString();
        final String search = editTextName.getText().toString().toLowerCase();
        final String videoUrl = editTextLink.getText().toString();
        if(videoName.equals("") || videoUrl.equals(""))
        {
            Toast.makeText(UploadVideoActivity.this, "Provide all details", Toast.LENGTH_LONG).show();
            return;
        }
        member.setName(videoName);
        member.setSearch(search);
        member.setVideourl(videoUrl);
        member.setSubject(subject + class_name + chapter_no);
        member.setType("youtube");
        db.collection("videos").add(member).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(UploadVideoActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UploadVideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}