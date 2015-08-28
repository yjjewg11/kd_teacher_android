package com.wjkj.kd.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class LoadUrlActivity extends BaseActivity {

    private WebView webview2;
//    public static String URLL = "http://wapbaike.baidu.com/view/4850574.htm?sublemmaid=" +
//            "15923552&adapt=1&fr=aladdin&target=_blank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_url);
        setViews();
    }
    private void setViews() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webview2 = (WebView)findViewById(R.id.webView2);
        webview2.loadUrl(url);
        webview2.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        LoadUrlActivity.this.finish();
    }
}