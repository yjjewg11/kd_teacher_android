package com.wjkj.kd.teacher.biz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.utils.HttpUtils;
import com.wjkj.kd.teacher.utils.ImageLoaderUtils;
import com.wjkj.kd.teacher.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SettingWebParams {

    private WebSettings webSettings;
    private String imgurl;

    //通用的webView和webSettings的设置参数
    public void setWebs(WebView webView) {

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("TAG", "长按点击事件");
                WebView.HitTestResult result = ((WebView) v).getHitTestResult();
                if (result != null) {
                    int type = result.getType();
                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        imgurl = result.getExtra();
                        Log.i("TAG", "打印" + imgurl);
                    }else {
                        return false;
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.instance);
                builder.setTitle("友情提示")
                        .setMessage("你确定要保存图片?")
//                        .setIcon(R.drawable.logo128)

                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("TAG", "确认图片地址没有问题" + MainActivity.instance.httpPicUrl);

                                ImageLoaderUtils.downLoadImageLoader(imgurl,
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
//
                                                String fileName = System.currentTimeMillis() + ".jpg";
                                                saveImageToGallery(fileName,MainActivity.instance,loadedImage);
//
//                                                ToastUtils.showMessage("图片保存成功");
//                                                MainActivity.instance.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/image.png"))));


                                            }

                                            @Override
                                            public void onLoadingCancelled(String imageUri, View view) {

                                            }

                                            public void saveImageToGallery(String fileName, Context context, Bitmap bmp) {
                                                // 首先保存图片
                                                File appDir = new File(Environment.getExternalStorageDirectory(), "问界互动家园");
                                                if (!appDir.exists()) {
                                                    appDir.mkdir();

                                                }
                                                File file = new File(appDir, fileName);
                                                try {
                                                    FileOutputStream fos = new FileOutputStream(file);
                                                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                                    fos.flush();
                                                    fos.close();
                                                } catch (FileNotFoundException e) {
                                                    ToastUtils.showMessage("图片保存失败");
                                                    e.printStackTrace();
                                                } catch (IOException e) {
                                                    ToastUtils.showMessage("图片保存失败");
                                                    e.printStackTrace();
                                                }

                                                // 其次把文件插入到系统图库
                                                try {
                                                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                                            file.getAbsolutePath(), fileName, null);
                                                } catch (FileNotFoundException e) {
                                                    ToastUtils.showMessage("图片保存失败");
                                                    e.printStackTrace();
                                                }
                                                // 最后通知图库更新
//                                                CGLog.d(Uri.fromFile(new File(file.getPath())) + "");
                                                Intent intent = new Intent();
                                                Log.i("TAG","打印图片的地址getAbsolutePath"+file.getAbsolutePath()+" 打印getPath "+file.getPath()+"   大一Uri.fromfile"+Uri.fromFile(new File(file.getPath())));
                                                Log.i("TAG","打印早此用"+Uri.parse("file://"+file.getAbsolutePath()));
                                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(file.getPath()))));
//        new SingleMediaScanner(mContext, new File(file.getPath()), null);
                                                ToastUtils.showMessage("图片保存成功");
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
