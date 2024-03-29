package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;

public class ParentFeedback extends AppCompatActivity {

    WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_feedback);

        mWebview  = new WebView(this);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

        mWebview .loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSd0Oet_aZIZ6jNVFSBK1FdUCKXhDZKZi3Af6rp5Lhr5WNZHJQ/viewform?usp=sf_link");
        setContentView(mWebview );
    }

    @Override
    public void onBackPressed() {
        if(mWebview.canGoBack())
        {
            mWebview.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }
}