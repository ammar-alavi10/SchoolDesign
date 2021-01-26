package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class ParentAlert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_alert);
    }

    public void NormalAlert(View view) {
        startActivity(new Intent(ParentAlert.this, NormalAlert.class));
    }

    public void StudentFeedback(View view) {
        startActivity(new Intent(ParentAlert.this, ParentFeedback.class));
    }

    public void SuperAlert(View view) {
        try {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address", "06263841076");
            smsIntent.putExtra("sms_body", "Super Alert");
            smsIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(smsIntent);
        } catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(ParentAlert.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}