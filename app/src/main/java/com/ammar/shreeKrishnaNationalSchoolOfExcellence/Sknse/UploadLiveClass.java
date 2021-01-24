package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.VideoModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadLiveClass extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;
    VideoView videoView;
    Button button;
    ProgressBar progressBar;
    EditText editTextName, class_et;
    EditText editTextLink;
    private Uri videoUri;
    MediaController mediaController;
    StorageReference storageReference;
    FirebaseFirestore db;
    VideoModel member;
    UploadTask uploadTask;
    String class_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_live_class);

        member = new VideoModel();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.et_video_name);
        editTextLink = findViewById(R.id.youtube_link_et);
        class_et = findViewById(R.id.et_class_name);
    }

    public void OnLinkUploadPressed(View view) {
        final String videoName = editTextName.getText().toString();
        final String search = editTextName.getText().toString().toLowerCase();
        final String videoUrl = editTextLink.getText().toString();
        class_name = class_et.getText().toString();
        if(videoName.equals("") || videoUrl.equals(""))
        {
            Toast.makeText(UploadLiveClass.this, "Provide all details", Toast.LENGTH_LONG).show();
            return;
        }
        member.setName(videoName);
        member.setSearch(search);
        member.setVideourl(videoUrl);
        member.setSubject(class_name);
        member.setType("youtube");
        db.collection("livevideos").add(member).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(UploadLiveClass.this, "Data saved", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(UploadLiveClass.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}