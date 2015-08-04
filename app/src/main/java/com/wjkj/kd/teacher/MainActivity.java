package com.wjkj.kd.teacher;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.biz.Menu;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.biz.MyAsyncTask;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.receiver.MyPushMessageReceiver;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.BitmapUtils;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.HttpUtils;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.ParseUtils;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils.ToastUtils;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

public class MainActivity extends BaseActivity {

    public static Date dateBegin = new Date();
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE_TEXT_JSON = "text/json";

    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    private static final int CROP_A_PICTURE = 10;
    private static final int CHOOSE_PICTURE_ONLY = 20;
    private Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
    private static final int RESULT_PICK_PHOTO_NORMAL = 1;
    public static int PUSH_STATE = 0;
    public static final String CANCLE_USER = "javascript:G_jsCallBack.user_info_logout()";
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    public WebView webView;
    public static String JESSIONID ;
    private RadioGroup radioGroup;
    public static MainActivity instance;
    public MyAsyncTask myAsyncTask;
    public static String URL = "http://wapbaike.baidu.com/view/4850574.htm?sublemmaid=" +
            "15923552&adapt=1&fr=aladdin&target=_blank";
//    public static String ServerURL = "http://120.25.248.31/px-rest/";
    //正式环境
//    public static String ServerURL = "http://kd.wenjienet.com/px-rest/";

    //调试用
    public static String ServerURL = "http://192.168.0.107:8080/px-rest/";
    public static String InterfaceURL = ServerURL+"rest/";
    public static String HTTPURL = ServerURL+"kd/index.html";



    public static String appCachePath = MyApplication.instance.getCacheDir().getAbsolutePath();
//            "http://120.25.248.31/px-rest/kd/index.html"
            ;
    private ValueCallback<Uri> myUploadMsg;

    private String tureth = "true";
    private TextView tv_permit;
    private ProgressBar progressBar;
    private Menu menu;
    public Handler handler =  new Handler();
    public WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymain);
        instance = this;

        menu = new Menu(this);

        setViews();

        StatService.bindJSInterface(this, webView);


    }

    public void hideText(){

        handler.post(new Runnable() {
            @Override
            public void run() {
                tv_permit.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
        });

        handler.sendEmptyMessage(1);

    }
    private void setViews() {
        tv_permit = (TextView)findViewById(R.id.textView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        webView = (WebView)findViewById(R.id.mainWebView);
        radioGroup = (RadioGroup)findViewById(R.id.first_page_radiogroup);
        setWebs();
    }
    boolean f = true;
    @Override
    public void onBackPressed() {

        if(tv_permit.getVisibility()==View.VISIBLE&&progressBar.getVisibility()==View.VISIBLE){
            finishAll();
        }
        webView.loadUrl("javascript:G_jsCallBack.QueuedoBackFN()");
    }
    private void finishAll() {
        if(f){
            ToastUtils.showMessage("再按一次退出程序");
            f = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    f = true;
                }
            }, 5000);
        }else{
            myAsyncTask.cancel(true);
            for(Activity activity : MyApplication.list){
                activity.finish();
            }

            //过两天清理一次
           long time =  (new Date().getTime())-dateBegin.getTime();
            if((time/3600/24)>=2){
                clear();
                dateBegin.setTime(new Date().getTime());
            }
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        boolean t = true;
//        if(keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
//            webView.goBack();
//
//        }else{
//            onBackPressed();
//        }
//        return onKeyLongPress(keyCode, event);
//    }

    private void setWebs() {
        webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        //支持缩放
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启缓存数据库功能
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        //设置缓存模式

        //设置缓存路径
        webSettings.setAppCachePath(appCachePath+"/clear");
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        //开启缓存功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webSettings.setDomStorageEnabled(true);
        // Set cache size to 2 mb by default. should be more than enough
        webSettings.setAppCacheMaxSize(1024 * 1024 * 2);
        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line.
        // UPDATE: no hardcoded path. Thanks to Kevin Hawkins


        webView.addJavascriptInterface(new JavaScriptCallSon(),"JavaScriptCall");
        webView.loadUrl(
                HTTPURL
//                "http://192.168.0.110:8080/px-rest/kd/"
        );
    }
    //此类用于和js交互
    class JavaScriptCallSon implements JavaScriptCall{
        //进入选择图片页面
        //此方法选择可裁剪的图片
        @JavascriptInterface
        public void selectHeadPic(){
            Intent intent = null;
            if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            }else{
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, RESULT_PICK_PHOTO_NORMAL);
        }

        //此方法不需要裁剪
        @JavascriptInterface
        public void selectImgPic(){
            Intent intent = null;
            if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            }else{
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, CHOOSE_PICTURE_ONLY);
        }




        //调用此方法取消提示


        //js调用此方法将JessionId传过来将其保存
        @JavascriptInterface
        public void jsessionToPhone(String jessionID){
            JESSIONID = jessionID;
            Log.i("TAG", "jsessionId===" + jessionID);

            //调用pushMessageToServer方法将渠道号和编号传到服务器
            try {
//                if(JESSIONID!=null)
//                pushMessageToServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //此方法选择图片并压缩，不需裁剪


        @JavascriptInterface
        public void hideLoadingDialog(){

            try {
                hideText();

            }catch (Exception e){
                e.printStackTrace();
                throw new RuntimeException(e);

            }
        }

        //此方法在js回退到底之后调用来退出程序
        @JavascriptInterface
        public void finishProject(){

          finishAll();
        }

    }
    //此方法调用网络请求传递参数
    public void pushMessageToServer() throws UnsupportedEncodingException, JSONException {

        //TODO
        MyPushMessageReceiver.f = false;
        String url =InterfaceURL+ "pushMsgDevice/save.json";
//                +"?JSESSIONID="+JESSIONID;
        JSONObject jSONObject=new JSONObject();
        jSONObject.put("device_id",MyPushMessageReceiver.CHANNL_ID);
        jSONObject.put("device_type","android");
        jSONObject.put("status",PUSH_STATE);
        StringEntity se = new StringEntity(jSONObject.toString(), HTTP.UTF_8);
//        se.setContentType(APPLICATION_JSON + HTTP.CHARSET_PARAM +HTTP.UTF_8);
        se.setContentEncoding(HTTP.UTF_8);
        RequestParams requestParams = new RequestParams();
        requestParams.put("JSESSIONID",JESSIONID);
//        requestParams.put("device_id", MyPushMessageReceiver.CHANNL_ID);
//        Log.i("TAG","打印渠道号码"+MyPushMessageReceiver.CHANNL_ID);
//        requestParams.put("device_type","android");
//        requestParams.put("status","0");
//        asyncHttpClient.post()
        String contentType = APPLICATION_JSON + HTTP.CHARSET_PARAM +HTTP.UTF_8;
        Header[] headers;
        headers = new BasicHeader[]{
                new BasicHeader("Cookie","JSESSIONID="+JESSIONID+";")
        };

        asyncHttpClient.post(this,url,headers,se,contentType,new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {

                    String shuzu = new String(bytes);
                    JSONObject jsonObject = new JSONObject(shuzu);
                    HttpUtils.pullJson(jsonObject);
                } catch (JSONException e) {
                    ExUtil.e(e);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ToastUtils.showMessage("网络连接处上传失败");
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
//            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 198);
            intent.putExtra("outputY", 198);
            intent.putExtra("scale","true");
            intent.putExtra("circleCrop","true");
            // 设置为true直接返回bitmap
            intent.putExtra("return-data", false);
            intent.putExtra("output", imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, CROP_A_PICTURE);

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PushManager.startWork(this,
                PushConstants.LOGIN_TYPE_API_KEY,
                "El4au0Glwr7Xt8sPgZFg2UP7");
        myAsyncTask = new MyAsyncTask();
        this.myAsyncTask.execute();
    }

    @Override
    protected void onDestroy() {

        MyPushMessageReceiver.f = true;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Cursor cursor = null;
        try {
            if (requestCode == RESULT_PICK_PHOTO_NORMAL && data != null) {
                String filePath = null;
                Uri uri = data.getData();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    filePath = getPath(uri);
                } else {
                    String[] cloums = {MediaStore.Images.Media.DATA};
                    cursor = getContentResolver().query(uri, cloums, null, null, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(0);
                }
                Log.i("TAG", "打印一下文件的路径" + filePath);
                cropImageUri(filePath);
            }
        }finally{

            if(cursor!=null)
            cursor.close();
            System.gc();

              }
              if(requestCode==CROP_A_PICTURE){
                  Log.i("TAG","打印问题是不是出在data身上");
//
                  Bitmap bitmap = null;
                  try {
                      String path = "/mnt/sdcard/temp.jpg";
                      bitmap = BitmapUtils.compressPictureFromFile(path);
                      String pictureBytes = ParseUtils.getBase64FromBitmap(bitmap);
                      pictureBytes =
                              "data:image/png;base64," + pictureBytes;
                      Log.i("if", "打印base" + pictureBytes);
                      Log.i("TAG", "返回了图片信息");
                      webView.loadUrl("javascript:G_jsCallBack.selectHeadPic_callback('" + pictureBytes + "')");
                  }catch (Exception e){
                      ExUtil.e(e);
                  }finally {
                      BitmapUtils.recyleBitmap(bitmap);
                  }
              }
             if(requestCode==CHOOSE_PICTURE_ONLY&&data!=null){
                 Bitmap bitmap = null;
                 try {
                     Uri uri = data.getData();
                     String storagePath = getPath(uri);
                     bitmap = BitmapUtils.compressPictureFromFile(storagePath);
                     String base = ParseUtils.getBase64FromBitmap(bitmap);
                     base = "data:image/png;base64," + base;
                     Log.i("if", "打印bsdfs" + base);
                     webView.loadUrl("javascript:G_jsCallBack.ajax_uploadByphone('" + base + "')");

                 }finally {
                     BitmapUtils.recyleBitmap(bitmap);
                 }
             }
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPath(Uri uri) {
        String myPath = null;
        Cursor cursor = null;
        try {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                String wholeID = DocumentsContract.getDocumentId(uri);
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                        sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    myPath = cursor.getString(columnIndex);
                }

            } else {
                String[] projection = {MediaStore.Images.Media.DATA};
                cursor = getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                myPath = cursor.getString(column_index);
            }
        }finally {
            if(cursor!=null)
            cursor.close();
            System.gc();
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
                //即使消息
                webView.loadUrl("javascript:G_jsCallBack.queryMyTimely_myList()");
                break;
            case R.id.radioButton4:
                //点击此按钮注销用户，并返回到登陆页面
                //回调javaScript方法

                webView.loadUrl(CANCLE_USER);
                break;
        }
    }

    class MyWebViewClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.i("info","打印url"+url.toString());

            if(url.contains("baidu.com")){
//                Intent intent = new Intent(MainActivity.this,LoadUrlActivity.class);
//                startActivity(intent);
                webView.loadUrl(URL);
            }
            return true;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            myUploadMsg = uploadMsg;
        }
    }

    public static MotionEvent motionEvent ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        motionEvent = event;
        return true;

    }

//    @Override
//    protected void onStop() {
//        PushManager.stopWork(this);
//        super.onStop();
//    }


    public void clear() {
        WebStorage webStorage = WebStorage.getInstance();
        webStorage.deleteAllData();
        MainActivity.instance.webView.clearCache(true);
        MainActivity.instance.webView.clearFormData();
        MainActivity.instance.webView.clearHistory();
        MainActivity.instance.webView.clearSslPreferences();
        MainActivity.instance.webView.clearMatches();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("info","看看方法横竖品几次");
        super.onConfigurationChanged(newConfig);
    }
}
