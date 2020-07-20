package com.ammar.schooldesign.KidsCorner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ammar.schooldesign.R;

public class SplashScreen extends AppCompatActivity {
    ProgressBar bar;
    TextView txt;
    int total = 0;
    boolean isRunning = false;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        bar = findViewById(R.id.ProgressBar1);
        txt = findViewById(R.id.txtrere);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    // Update the progress status
                    progressStatus += 1;

                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(progressStatus);
                            // Show the progress on TextView
                            txt.setText("Loading..." + progressStatus + "");
                            if (progressStatus == 100) {
                                Intent intent = new Intent(getApplicationContext(), KidsLandingPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        }).start(); // Start the operation
    }

}