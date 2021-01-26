package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

public class ForgotPassword extends AppCompatActivity {

    EditText username_et;
    String username;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        username_et = findViewById(R.id.username_et);
        mAuth = FirebaseAuth.getInstance();
    }

    public void sendResetLink(View view) {
        username = username_et.getText().toString();
        if(TextUtils.isEmpty(username))
        {
            username_et.setError("Please enter email id");
            username_et.requestFocus();
            return;
        }
        final LovelyProgressDialog progressDialog = new LovelyProgressDialog(this);
        progressDialog.setTitle("Sending Email");
        progressDialog.show();

        mAuth.sendPasswordResetEmail(username).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    showdialog("Reset email has been sent to your email id");
                }
                else{
                    progressDialog.dismiss();
                    showdialog("Problem in sending email");
                }
            }
        });
    }

    public void showdialog(String message)
    {

        final AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(ForgotPassword.this);

        // Setting Dialog Message
        alertDialog1.setMessage(message);

        // Setting OK Button
        alertDialog1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = alertDialog1.create();

        alertDialog.setTitle("Reset Link");

        // Showing Alert Message
        alertDialog1.show();
    }

    public void HomeClicked(View view) {
        Intent intent = new Intent(ForgotPassword.this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}