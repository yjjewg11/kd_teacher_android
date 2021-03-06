package com.wjkj.kd.teacher;
import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.leakcanary.LeakCanary;
import com.wjkj.kd.teacher.utils.GloableUtils;
import com.wjkj.kd.teacher.utils.ImageLoaderUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class MyApplication extends MultiDexApplication {
    public static MyApplication instance;
    //此集合用于存储activity对象
    public static ArrayList<Activity> list = new ArrayList();
    public String justUrl = "";
    private DisplayImageOptions options;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        list.clear();
        LeakCanary.install(this);
        configImagerLoader();
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



    private void configImagerLoader() {
        try{
            String cachePath = GloableUtils.cachePath;
            ImageLoaderUtils.initImageLoader(this, R.drawable.icon, cachePath, 10, 0);
//            options = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.icon)
//                    .showImageForEmptyUri(R.drawable.icon)
//                    .showImageOnFail(R.drawable.icon)
//                    .cacheInMemory(true)
//                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .cacheOnDisk(true)
//                    .displayer(new RoundedBitmapDisplayer(0)).build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        // 创建默认的ImageLoader配置参数
//        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
//                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
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


}
