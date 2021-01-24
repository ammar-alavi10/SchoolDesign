package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddHeadline extends AppCompatActivity {

    private AnnouncementModel announcementModel;
    String announcementTitle, announcementDesc;
    String class_name;
    EditText title, desc, class_et;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_headline);


        class_et = findViewById(R.id.announcement_class);
        title = findViewById(R.id.announcement_title);
        desc = findViewById(R.id.announcement_text);
        announcementModel = new AnnouncementModel();
        db = FirebaseFirestore.getInstance();
    }

    public void UploadAnnouncement(View view) {
        announcementTitle = title.getText().toString();
        announcementDesc = desc.getText().toString();
        class_name = class_et.getText().toString();
        if( !announcementTitle.equals("") && !announcementDesc.equals("") && !class_name.equals(""))
        {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            String formattedDate = df.format(c);
            announcementModel.setDate(formattedDate);
            announcementModel.setTitle(announcementTitle);
            announcementModel.setDescription(announcementDesc);
            announcementModel.setSubject(class_name);

            db.collection("headlines").add(announcementModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AddHeadline.this, "Data saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AddHeadline.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            Toast.makeText(AddHeadline.this, "All fields are required", Toast.LENGTH_SHORT).show();

        }
    }
}