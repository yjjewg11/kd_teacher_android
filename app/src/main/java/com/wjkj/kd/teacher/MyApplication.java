package com.wjkj.kd.teacher;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wjkj.kd.teacher.utils.GloableUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyApplication extends Application{
    public static MyApplication instance;
    //此集合用于存储activity对象
    public static ArrayList<Activity> list = new ArrayList();
    public String justUrl = "";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        list.clear();
//        configImagerLoader();
        //获取最新网页地址
        new AsyncHttpClient().get(this, GloableUtils.newUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    justUrl = (String) jsonObject.get("url");
                    Log.i("TAG","打印这个          ------------------值"+justUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.i("TAG","数据获取失败了");
            }
        });

    }



//    private void configImagerLoader() {
//
//        // 创建默认的ImageLoader配置参数
//        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.JPEG, 75, null)
//                .threadPoolSize(3) // default
//                .threadPriority(Thread.NORM_PRIORITY - 1) // default
//                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)
//                .memoryCacheSizePercentage(13) // default
//                .discCache(new UnlimitedDiscCache(getCacheDir())) // default
//                .discCacheSize(50 * 1024 * 1024)
//                .discCacheFileCount(100)
//                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
//                .imageDownloader(new BaseImageDownloader(this)) // default
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                .writeDebugLogs()
//                .build();
//        ImageLoader.getInstance().init(configuration);
//    }




}
