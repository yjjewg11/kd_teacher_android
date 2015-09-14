package com.wjkj.kd.teacher.biz;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wjkj.kd.teacher.utils.HttpUtils;

public class SettingWebParams {

    private WebSettings webSettings;

    //通用的webView和webSettings的设置参数
    public void setWebs(WebView webView) {


        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //支持缩放
        webSettings.setBuiltInZoomControls(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启缓存数据库功能set
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
//        webSettings.setBlockNetworkImage(true);
        //设置缓存模式

        //设置缓存路径
//        webSettings.setAppCachePath(appCachePath+"/clear");
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        //开启缓存功能
//        webSettings.setAppCacheEnabled(true);
        //根据网络判断设置使用哪种缓存功能
        setCache();
        // Set cache size to 2 mb by default. should be more than enough
        webSettings.setAppCacheMaxSize(1024 * 1024 * 10);
        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line.
        // UPDATE: no hardcoded path. Thanks to Kevin Hawkins


        webView.loadData("text/html", "utf-8", "utf-8");


    }

    private void setCache() {
        if(HttpUtils.getConntectStates()){
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        }else{
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    }
}
