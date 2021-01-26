package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LeaveApplication extends AppCompatActivity {

    EditText date_tv, appl_et;
    String date, appl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);

        date_tv = findViewById(R.id.date_tv);
        appl_et = findViewById(R.id.leaveappl_et);
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(LeaveApplication.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    public class LeaveModel{
        String date, application;

        public LeaveModel(String date, String application) {
            this.date = date;
            this.application = application;
        }

        public LeaveModel() {
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }
    }

    public void Submit(View view) {
        date = date_tv.getText().toString();
        appl = appl_et.getText().toString();
        if(TextUtils.isEmpty(date))
        {
            date_tv.requestFocus();
            date_tv.setError("Enter date");
            return;
        }
        if(TextUtils.isEmpty(appl))
        {
            appl_et.requestFocus();
            appl_et.setError("Enter date");
            return;
        }
        LeaveModel leaveModel = new LeaveModel(date, appl);
        FirebaseFirestore.getInstance().collection("leave-applications").document(FirebaseAuth.getInstance().getCurrentUser().getUid() + date)
                .set(leaveModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LeaveApplication.this, "Leave Application Submitted", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(LeaveApplication.this, "Problem in submitting leave application", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}