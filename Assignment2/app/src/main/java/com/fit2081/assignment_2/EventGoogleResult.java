package com.fit2081.assignment_2;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class EventGoogleResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_google_result);

        // Get event name from Intent
        String eventName = getIntent().getStringExtra("eventName");

        // Construct the Google search URL by appending the event name
        String googleSearchURL = "https://www.google.com/search?q=" + eventName;

        // Find the WebView in the layout
        WebView webView = findViewById(R.id.webView);

        // Set up WebView settings
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript (optional)
        webView.setWebViewClient(new WebViewClient()); // Handle navigation in the WebView

        // Load the Google search results page in the WebView
        webView.loadUrl(googleSearchURL);
    }
}
