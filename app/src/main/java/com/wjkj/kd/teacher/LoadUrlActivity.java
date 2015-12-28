package com.wjkj.kd.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wjkj.kd.teacher.bean.ShareContent;
import com.wjkj.kd.teacher.utils.ShareUtils;

public class LoadUrlActivity extends BaseActivity {

    private WebView webview2;
    private ShareContent content;
    private WebSettings webSettings;
    private RelativeLayout rl_back;
    private RelativeLayout rl_share;
    private TextView center_view;
//    public static String URLL = "http://wapbaike.baidu.com/view/4850574.htm?sublemmaid=" +
//            "15923552&adapt=1&fr=aladdin&target=_blank";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_url);
        setViews();
    }
    private void setViews() {

        initViews();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        content = (ShareContent) intent.getSerializableExtra("shareContent");
        webview2 = (WebView)findViewById(R.id.web_load_html);
        webSettings = webview2.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启缓存数据库功能set
        webSettings.setUseWideViewPort(true);
        webview2.setWebChromeClient(new WebChromeClient());
        webview2.setWebViewClient(new WebViewClient());

        if(!TextUtils.isEmpty(url)){
            webview2.loadUrl(url);
        }
        if(content != null){
            webview2.loadUrl(content.getHttpurl());
            center_view.setText(""+content.getTitle());
            rl_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.showShareDialog(LoadUrlActivity.this, v, content.getTitle(), content.getContent(), content.getPathurl(), content.getHttpurl());
                }
            });

        }

    }

    private void initViews() {
        rl_back = (RelativeLayout)findViewById(R.id.html_rl_back);

        rl_share = (RelativeLayout)findViewById(R.id.html_share);

        center_view = (TextView)findViewById(R.id.html_center_text);
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