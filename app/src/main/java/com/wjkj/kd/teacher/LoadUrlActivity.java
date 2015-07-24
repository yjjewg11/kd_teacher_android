package com.wjkj.kd.teacher;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class LoadUrlActivity extends Activity {

    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_url);

        setViews();
    }
    private void setViews() {
        webview = (WebView)findViewById(R.id.webView2);
        webview.loadUrl(MainActivity.URL);
        webview.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}