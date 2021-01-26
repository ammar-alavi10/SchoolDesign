package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Birthday extends AppCompatActivity {

    Date todayDate, bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        String dob = getIntent().getStringExtra("dob");

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            todayDate = dateFormatter.parse(dateFormatter.format(new Date() ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            if (dob != null) {
                bd = dateFormatter.parse(dob);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTime(todayDate);
        cal2.setTime(bd);



        if(cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR))
        {
            ImageView imageView = findViewById(R.id.birthday_card);
            imageView.setVisibility(View.VISIBLE);
            TextView textView = findViewById(R.id.birthday_text);
            textView.setVisibility(View.GONE);
        }

    }
}