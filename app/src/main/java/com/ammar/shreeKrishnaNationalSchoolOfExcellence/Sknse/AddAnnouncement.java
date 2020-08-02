package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.Models.AnnouncementModel;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddAnnouncement extends AppCompatActivity {

    private AnnouncementModel announcementModel;
    String announcementTitle, announcementDesc;
    String class_name, subject_name;
    EditText title, desc;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);

        subject_name = getIntent().getStringExtra("subject_name");
        class_name = getIntent().getStringExtra("class_name");

        title = findViewById(R.id.announcement_title);
        desc = findViewById(R.id.announcement_text);
        announcementModel = new AnnouncementModel();
        db = FirebaseFirestore.getInstance();
    }

    public void ShowAnnouncements(View view) {
        Intent intent = new Intent(AddAnnouncement.this, ShowAnnouncementList.class);
        intent.putExtra("subject_name",subject_name);
        intent.putExtra("class_name", class_name);
        startActivity(intent);
    }

    public void UploadAnnouncement(View view) {
        announcementTitle = title.getText().toString();
        announcementDesc = desc.getText().toString();
        if( !announcementTitle.equals("") && !announcementDesc.equals(""))
        {
            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            announcementModel.setDate(formattedDate);
            announcementModel.setTitle(announcementTitle);
            announcementModel.setDescription(announcementDesc);
            announcementModel.setSubject(subject_name + class_name);

            db.collection("announcements").add(announcementModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AddAnnouncement.this, "Data saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddAnnouncement.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(AddAnnouncement.this, "All fields are required", Toast.LENGTH_SHORT).show();

        }
    }
}