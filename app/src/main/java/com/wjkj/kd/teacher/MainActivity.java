package com.wjkj.kd.teacher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.receiver.MyPushMessageReceiver;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class MainActivity extends BaseActivity {

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    private static final int CROP_A_PICTURE = 10;
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
    private static final int RESULT_PICK_PHOTO_NORMAL = 1;
    private WebView webView;
    public static String JESSIONID ;
    private RadioGroup radioGroup;
    public static Context instance;
    public static String HTTPURL = "http://120.25.248.31/px-rest/kd/index.html";
    public static String URL = "http://wapbaike.baidu.com/view/4850574.htm?sublemmaid=" +
            "15923552&adapt=1&fr=aladdin&target=_blank";
    private ValueCallback<Uri> myUploadMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        setViews();
        StatService.setAppKey("4311ed70ee");
        StatService.setAppChannel(this, "null", false);
        //启动推送服
        PushManager.startWork(this,
                PushConstants.LOGIN_TYPE_API_KEY,
                "El4au0Glwr7Xt8sPgZFg2UP7");
        StatService.bindJSInterface(this, webView);
    }


    private void setViews() {
        webView = (WebView)findViewById(R.id.mainWebView);
        radioGroup = (RadioGroup)findViewById(R.id.first_page_radiogroup);
        Log.i("TAG","初始化正常");
        setWebs();
    }

    private void setWebs() {
        Log.i("TAG","初始化第二次正常");
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);

        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(HTTPURL);
        webSettings.setDomStorageEnabled(true);

        webSettings.setDatabaseEnabled(true);

        webSettings.setUseWideViewPort(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webSettings.setDomStorageEnabled(true);
        // Set cache size to 8 mb by default. should be more than enough
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line.
        // UPDATE: no hardcoded path. Thanks to Kevin Hawkins
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        JavaScriptCall javaScriptCall = new JavaScriptCallSon();
        webView.addJavascriptInterface(javaScriptCall,"JavaScriptCall");


    }

    //此类用于和js交互
    class JavaScriptCallSon implements JavaScriptCall{
        //进入选择图片页面
        public void selectHeadPic(){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, RESULT_PICK_PHOTO_NORMAL);
        }

        //js调用此方法将JessionId传过来将其保存
        public void getJessionId(String jessionID){
            JESSIONID = jessionID;
            //调用pushMessageToServer方法将渠道号和编号传到服务器
            pushMessageToServer();

        }


    }

    //此方法调用网络请求传递参数
    private void pushMessageToServer() {
        //TODO
        String url = "http://localhost:8080/px-moblie/rest/pushMsgDevice/save.json"
                +"?JSESSIONID="+JESSIONID;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("device_id", MyPushMessageReceiver.CHANNL_ID);
        requestParams.put("device_type","android");
        requestParams.put("status","0");
        asyncHttpClient.post(url,requestParams,new AsyncHttpResponseHandler(){


            @Override
            public void onSuccess(String content) {

                try {
                    JSONObject jsonObject = new JSONObject(content);
                    HttpUtils.pullJson(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error) {

            }
        });

    }

    private void cropImageUri(String path) {
        File file = new File(path);
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 600);
        // 设置为true直接返回bitmap
        intent.putExtra("return-data", false);
        intent.putExtra("output", imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, CROP_A_PICTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
              if(requestCode==RESULT_PICK_PHOTO_NORMAL&&resultCode==RESULT_OK){
                  Uri uri = data.getData();
                  cropImageUri(uri.getPath());

              }
              if(requestCode==CROP_A_PICTURE&&resultCode==RESULT_OK){
                  ByteArrayOutputStream out = new ByteArrayOutputStream();
                  Uri uri = data.getData();
                  Bitmap bitmap =  BitmapFactory.decodeFile(uri.getPath());
                  bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
                  byte [] bytes = out.toByteArray();
                  String pictureBytes = Base64.encodeToString(bytes,Base64.DEFAULT);
                  webView.loadUrl("javascript:jsessionToPhone('" + pictureBytes + "')");

              }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radioButton1:

                webView.loadUrl("javascript:menu_dohome()");
                break;
            case R.id.radioButton2:
                break;
            case R.id.radioButton3:
                break;
            case R.id.radioButton4:
                break;
        }
    }

    class MyWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.contains("baidu.com")){
                Intent intent = new Intent(MainActivity.this,LoadUrlActivity.class);
                startActivity(intent);
            }

            return true;
        }

    }

    private class MyWebChromeClient extends WebChromeClient {

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            myUploadMsg = uploadMsg;
        }
    }
}
