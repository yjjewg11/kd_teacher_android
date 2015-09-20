package com.wjkj.kd.teacher.biz;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wjkj.kd.teacher.LoadUrlActivity;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.MyApplication;

public  class MyOwnWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView v, String url) {
        Log.i("TAG","打印长按图片的时候有没有这个地址"+url);
        Log.i("TAG", "打印这个方法有没有执行看看url地址" + url.toString());
        if(url.contains("tel:")){
            int num = url.indexOf(":");
            String tel = url.substring(num);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
            MainActivity.instance.startActivity(intent);
        }else{
            Intent intent = new Intent(MyApplication.instance, LoadUrlActivity.class);
            intent.putExtra("url",url);
            MainActivity.instance.startActivity(intent);
        }
        return true;
    }
}