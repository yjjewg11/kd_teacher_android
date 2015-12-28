package com.wjkj.kd.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.wjkj.kd.teacher.bean.ShareContent;
import com.wjkj.kd.teacher.utils.ShareUtils;

public class LoadUrlActivity extends BaseActivity {

    private WebView webview2;
    private TextView tv_share_content;
    private ShareContent content;
    private WebSettings webSettings;
//    public static String URLL = "http://wapbaike.baidu.com/view/4850574.htm?sublemmaid=" +
//            "15923552&adapt=1&fr=aladdin&target=_blank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_url);
        setViews();
    }
    private void setViews() {

        tv_share_content = (TextView)findViewById(R.id.tv_share_content);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        content = (ShareContent) intent.getSerializableExtra("shareContent");
        webview2 = (WebView)findViewById(R.id.webView2);
        webSettings = webview2.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启缓存数据库功能set
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);

        if(!TextUtils.isEmpty(url)){
            webview2.loadUrl(url);
        }
        if(content != null){
            tv_share_content.setVisibility(View.VISIBLE);
            webview2.loadUrl(content.getHttpurl());
            tv_share_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.showShareDialog(LoadUrlActivity.this,v,content.getTitle(),content.getContent(),content.getPathurl(),content.getHttpurl());
                }
            });
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webview2.canGoBack()){
            webview2.goBack();
        }else{
            finish();
        }
        return true;
    }
}