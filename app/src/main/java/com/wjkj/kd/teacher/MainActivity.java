package com.wjkj.kd.teacher;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioGroup;

import com.baidu.mobstat.StatService;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.receiver.MyPushMessageReceiver;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.BitmapUtils;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.HttpUtils;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.ParseUtils;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;


public class MainActivity extends BaseActivity {

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    private static final int CROP_A_PICTURE = 10;
    private static final int CHOOSE_PICTURE_ONLY = 20;
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
    private static final int RESULT_PICK_PHOTO_NORMAL = 1;
    public WebView webView;
    public static String JESSIONID ;
    private RadioGroup radioGroup;
    public static MainActivity instance;
    public static String PUSHHTTP = "http://120.25.248.31/px-rest/";
    public static String HTTPURL = "http://120.25.248.31/px-rest/kd/index.html";
    public static String URL = "http://wapbaike.baidu.com/view/4850574.htm?sublemmaid=" +
            "15923552&adapt=1&fr=aladdin&target=_blank";
    private ValueCallback<Uri> myUploadMsg;
    private String filePath;
    private String tureth = "true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        setViews();
        StatService.bindJSInterface(this, webView);

    }


    private void setViews() {
        webView = (WebView)findViewById(R.id.mainWebView);
        radioGroup = (RadioGroup)findViewById(R.id.first_page_radiogroup);
        setWebs();
    }

    boolean f = true;
    @Override
    public void onBackPressed() {
        if(f){
            ToastUtils.showMessage("再按一次退出程序");
            f = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    f = true;
                }
            },5000);
        }else{
            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean t = true;
        if(keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
            webView.goBack();
            t = true;
        }else{
            t = false;
        }
        return t;
    }

    private void setWebs() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
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
        webView.addJavascriptInterface(new JavaScriptCallSon(),"JavaScriptCall");
        webView.loadUrl(HTTPURL);
    }
    //此类用于和js交互
    class JavaScriptCallSon implements JavaScriptCall{
        //进入选择图片页面
        //此方法选择可裁剪的图片
        @JavascriptInterface
        public void selectHeadPic(){
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, RESULT_PICK_PHOTO_NORMAL);

        }

//        @JavascriptInterface
//        public void selectHeadPic(String crop){
//            if(crop!=null){tureth = "false";}
//            selectHeadPic();
//        }

        //js调用此方法将JessionId传过来将其保存
        @JavascriptInterface
        public void jsessionToPhone(String jessionID){

            JESSIONID = jessionID;
            Log.i("TAG","jsessionId==="+MyPushMessageReceiver.CHANNL_ID );
            //调用pushMessageToServer方法将渠道号和编号传到服务器
            pushMessageToServer();
        }

        //此方法选择图片并压缩，不需裁剪
        @JavascriptInterface
        public void selectImgPic(){
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, CHOOSE_PICTURE_ONLY);
        }
    }
    //此方法调用网络请求传递参数
    private void pushMessageToServer() {
        //TODO
        String url =PUSHHTTP+ "pushMsgDevice/save.json"
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

        try {
            File file = new File(path);
            Intent intent = new Intent("com.android.camera.action.CROP");
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 2);
            intent.putExtra("outputX", 400);
            intent.putExtra("outputY", 600);
            // 设置为true直接返回bitmap
            intent.putExtra("return-data", true);
            startActivityForResult(intent, CROP_A_PICTURE);
            Log.i("TAG","图片裁剪程序已正常启动");
        }catch (Exception e){
            Log.i("TAG","裁剪图片出错了");
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
              if(requestCode==RESULT_PICK_PHOTO_NORMAL&&data!=null){
                  Uri uri = data.getData();
                  filePath = getPath(uri);
                  Log.i("TAG","打印一下文件的路径"+filePath);
                  cropImageUri(filePath);
              }
              if(requestCode==CROP_A_PICTURE){
                  Log.i("TAG","打印问题是不是出在data身上");
                  Log.i("TAG","返回了图片信息");
                  Bundle extras = data.getExtras();
//                  　　//获得实际剪裁的区域的bitmap图形
                  Bitmap thePic = extras.getParcelable("data");
                  String pictureBytes = ParseUtils.getBase64FromBitmap(thePic);
                  pictureBytes="data:image/png;base64,"+pictureBytes;
                  Log.i("TAG","查看base"+pictureBytes);
                  webView.loadUrl("javascript:returnHeadPicPicture('" + pictureBytes + "')");
              }
             if(requestCode==CHOOSE_PICTURE_ONLY&&data!=null){
                 Uri uri = data.getData();
                 String storagePath = getPath(uri);
                 Bitmap bitmap =  BitmapUtils.compressPictureFromFile(storagePath);
                 String base = ParseUtils.getBase64FromBitmap(bitmap);
                 base="data:image/png;base64,"+base;
                 webView.loadUrl("javascript:uploadByphone('"+ base +"')");
             }
    }



    private String getPath(Uri uri) {
        String myPath = null;
        if(DocumentsContract.isDocumentUri(this, uri)){
            String wholeID = DocumentsContract.getDocumentId(uri);
            String id = wholeID.split(":")[1];
            String[] column = { MediaStore.Images.Media.DATA };
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                    sel, new String[] { id }, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                myPath = cursor.getString(columnIndex);
            }
            cursor.close();
        }else{
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            myPath = cursor.getString(column_index);
        }

        return myPath;
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
            Log.i("TAG","打印一下url"+url);
            if(url.contains("baidu.com")){
                Intent intent = new Intent(MainActivity.this,LoadUrlActivity.class);
                startActivity(intent);
            }

            return true;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private class MyWebChromeClient extends WebChromeClient {

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            myUploadMsg = uploadMsg;
        }
    }
}
