package com.wjkj.kd.teacher.biz;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wjkj.kd.teacher.LoadUrlActivity;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.MyApplication;

public  class MyOwnWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView v, String url) {
        if(url.startsWith("tel:")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
            MainActivity.instance.startActivity(intent);
        }else{
            if(url.contains("wenjienet.com")) {
                MainActivity.instance.webView.reload();
                return true;
            }
            Intent intent = new Intent(MyApplication.instance, LoadUrlActivity.class);
            intent.putExtra("url",url);
            MainActivity.instance.startActivity(intent);
        }
        return true;
    }
}