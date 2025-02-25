package com.example.youtube_java.ui.theme;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.youtube_java.R;

public class VideoPlayerActivity extends AppCompatActivity {

    private WebView webView;
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        videoId = getIntent().getStringExtra("video_id");
        if (videoId == null || videoId.isEmpty()) {
            Toast.makeText(this, "Error: No video ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize WebView
        webView = findViewById(R.id.webView);
        setupWebView();
        loadYoutubeVideo();
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
    }

    private void loadYoutubeVideo() {
        // Create an HTML content with iframe to play YouTube video
        String htmlContent = "<html><body style='margin:0;padding:0;'>" +
                "<iframe width='100%' height='100%' src='https://www.youtube.com/embed/" + videoId +
                "?autoplay=1' frameborder='0' allowfullscreen></iframe>" +
                "</body></html>";

        webView.loadData(htmlContent, "text/html", "utf-8");
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}