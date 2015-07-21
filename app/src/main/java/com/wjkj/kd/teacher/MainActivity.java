package com.wjkj.kd.teacher;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class MainActivity extends Activity {

    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
    }

    private void setViews() {
        webView = (WebView)findViewById(R.id.webView);
        webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        //����֧������
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://120.25.248.31/px-rest/kd/index.html");
    }
}
