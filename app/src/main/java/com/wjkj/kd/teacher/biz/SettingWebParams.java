package com.wjkj.kd.teacher.biz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.R;
import com.wjkj.kd.teacher.utils.HttpUtils;
import com.wjkj.kd.teacher.utils.ImageLoaderUtils;
import com.wjkj.kd.teacher.utils.ToastUtils;

public class SettingWebParams {

    private WebSettings webSettings;

    //通用的webView和webSettings的设置参数
    public void setWebs(WebView webView) {

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("TAG", "长按点击事件");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.instance);
                builder.setTitle("友情提示")
                        .setMessage("你确定要保存图片?")
                        .setIcon(R.drawable.icon)

                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("TAG", "确认图片地址没有问题" + MainActivity.instance.httpPicUrl);
                                if(!MainActivity.instance.httpPicUrl.endsWith(".png")) {
                                    ToastUtils.showMessage("图片地址无效");
                                    return;
                                }
                                ImageLoaderUtils.downLoadImageLoader(MainActivity.instance.httpPicUrl,
                                        new ImageLoadingListener() {
                                            @Override
                                            public void onLoadingStarted(String imageUri, View view) {

                                            }

                                            @Override
                                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                                Log.i("TAG", "保存图片失败");
                                            }

                                            @Override
                                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                                MediaStore.Images.Media.insertImage(MainActivity.instance.getContentResolver(), loadedImage, "CG", "CG");

                                                ToastUtils.showMessage("图片保存成功");

                                            }

                                            @Override
                                            public void onLoadingCancelled(String imageUri, View view) {

                                            }
                                        });
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                return false;
            }
        });


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
    }
}
