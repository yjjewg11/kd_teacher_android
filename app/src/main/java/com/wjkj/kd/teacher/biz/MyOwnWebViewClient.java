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
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

//                if (url.startsWith("mailto:") || url.startsWith("geo:") ||url.startsWith("tel:")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    MyApplication.instance.startActivity(intent);
//                }

        Log.i("TAG", "打印这个方法有没有执行看看url地址" + url.toString());
        if(url.contains("tel:")){
            int num = url.indexOf(":");
            String tel = url.substring(num);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
            MainActivity.instance.startActivity(intent);
        }else if (url.contains("baidu.com")){
            MainActivity.instance.startActivity(new Intent(MyApplication.instance, LoadUrlActivity.class));
        }
        return true;
    }
}