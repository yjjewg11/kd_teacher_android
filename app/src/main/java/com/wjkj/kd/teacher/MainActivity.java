package com.wjkj.kd.teacher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;
import com.wjkj.kd.teacher.biz.DealWithPushMessage;
import com.wjkj.kd.teacher.biz.MyAsyncTask;
import com.wjkj.kd.teacher.biz.MyOwnWebViewClient;
import com.wjkj.kd.teacher.biz.MyWebChromeClient;
import com.wjkj.kd.teacher.biz.PushMessage;
import com.wjkj.kd.teacher.handle.MyUmengMessageHandle;
import com.wjkj.kd.teacher.interfaces.JavaScriptCall;
import com.wjkj.kd.teacher.receiver.ListenConntectStatesReceiver;
import com.wjkj.kd.teacher.receiver.MyPushMessageReceiver;
import com.wjkj.kd.teacher.utils.AnimationUtils;
import com.wjkj.kd.teacher.utils.BitmapUtils;
import com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.utils.GloableUtils;
import com.wjkj.kd.teacher.utils.ParseUtils;
import com.wjkj.kd.teacher.utils.ToastUtils;
import com.wjkj.kd.teacher.views.CustomFankuiActivity;
import com.wjkj.kd.teacher.views.MyRadioButton;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.List;

//import com.wjkj.kd.teacher.biz.Menu;

public class MainActivity extends BaseActivity {
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    public WebView webView;
    public RadioGroup radioGroup;
    public static MainActivity instance;
    public MyAsyncTask myAsyncTask;
    ;
    public ValueCallback<Uri> myUploadMsg;
    private String tureth = "true";
    private TextView tv_permit;
    private ProgressBar progressBar;
    //    private Menu menu;
    public static Handler handler;
    public WebSettings webSettings;
    public String myurl;
    public FeedbackAgent agent;
    public int screenHeight;
    public int screenWidth;
    public String device_token;
    public PushMessage pushMessage;
    public TextView tv_line;
    public MyRadioButton radionbtown;
    private ImageView imageLoading;
    private RelativeLayout animationRl;
    private ImageBroadcastReceiver imageBroadcastReceiver;
    private String picbase;
    public int width;
    public int height;
//    private TextView tv_down_animation;
    public String JESSIONID;
    private ListenConntectStatesReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //获得屏幕的尺寸

        getWidthSize();

        setContentView(R.layout.activity_mymain);
        setViews();
        register();
        agent = new FeedbackAgent(this);
        instance = this;
        try {
            //禁用侧滑菜单
//              menu = new Menu(this);
            initfankui();
            initUpdateApk();
            initPushMessage();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void register() {
        //注册接受图片地址的广播
        imageBroadcastReceiver = new ImageBroadcastReceiver();
        IntentFilter filter = new IntentFilter(me.nereo.multiimageselector.MainActivity.RESOLVE_IMAGE);
        registerReceiver(imageBroadcastReceiver, filter);

        //注册网络监听状态广播
        receiver = new ListenConntectStatesReceiver();
        IntentFilter confilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver,confilter);
    }

    private void getWidthSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Log.i("TAG", "打印屏幕的宽度" + width);
        if (width <= 480) {
            MyRadioButton.widthSize = 90;
        } else if (width <= 720) {
            MyRadioButton.widthSize = 120;
        } else if (width <= 1080) {
            MyRadioButton.widthSize = 160;
        }
    }

    //初始化友盟更新
    private void initUpdateApk() {
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setDefault();
        //设置自动更新监听
        UmengUpdateAgent.update(this);
        UpdateConfig.setDebug(true);
    }

    //友盟推送
    private void initPushMessage() throws UnsupportedEncodingException, JSONException {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        ownNotification(mPushAgent);
        mPushAgent.enable();
        device_token = UmengRegistrar.getRegistrationId(this);
        DealWithPushMessage.dealPushMessage();
    }

    //当通知来临时，点亮图标
    private void ownNotification(PushAgent mPushAgent) {

        //通知来临时，加上自己的业务处理逻辑
        UmengMessageHandler umengMessageHandler = new MyUmengMessageHandle();

        mPushAgent.setMessageHandler(umengMessageHandler);
    }

    //友盟反馈
    private void initfankui() {
        agent = new FeedbackAgent(this);
        agent.setWelcomeInfo();
        agent.setWelcomeInfo("欢饮来到反馈中心，您宝贵的意见是对我们最大的支持");

        FeedbackPush.getInstance(this).init(CustomFankuiActivity.class, true);
        agent.sync();

        agent.openFeedbackPush();

        FeedbackPush.getInstance(this).init(true);
    }


    public void hideText() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                tv_permit.setVisibility(View.GONE);
                animationRl.setVisibility(View.GONE);


            }
        });
        handler.sendEmptyMessage(1);
    }

    private void setViews() {
        tv_permit = (TextView) findViewById(R.id.tv_down_animation);
        imageLoading = (ImageView) findViewById(R.id.imageloading);
        imageLoading.setVisibility(View.VISIBLE);
        imageLoading.startAnimation(getDialogAnimation());
        animationRl = (RelativeLayout) findViewById(R.id.animation_rl);
        webView = (WebView) findViewById(R.id.mainWebView);
        radioGroup = (RadioGroup) findViewById(R.id.first_page_radiogroup);
        radionbtown = (MyRadioButton) findViewById(R.id.radioButton3);
        tv_line = (TextView) findViewById(R.id.tv_above_white_line);
        AnimationUtils.hideView(tv_line);
//        tv_line.setVisibility(View.GONE);
        AnimationUtils.hideRadio(radioGroup);
        getWidthAndHeight();
        //设置webview的一些参数
        setWebs();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 100:
                        webView.loadUrl("javascript:G_jsCallBack.selectPic_callback('" + picbase + "')");
                        break;
                }
            }
        };
        Log.i("TAG","handler刚刚初始化完毕,当前时间为"+System.currentTimeMillis());
    }

    private RotateAnimation getDialogAnimation() {
        RotateAnimation animation = new RotateAnimation(0f, 358.0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(400);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        return animation;
    }

    private void getWidthAndHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        Log.i("TAG", "打印屏幕的宽和高" + screenWidth + "     ===" + screenHeight);
    }

    boolean f = true;

    @Override
    public void onBackPressed() {

        if (tv_permit.getVisibility() == View.VISIBLE
//                && progressBar.getVisibility() == View.VISIBLE
                ) {
            finishAll();
        }
        webView.loadUrl("javascript:G_jsCallBack.QueuedoBackFN()");
//        super.onBackPressed();
    }

    private void finishAll() {
        if (f) {
            ToastUtils.showMessage("再按一次退出程序");
            f = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    f = true;
                }
            }, 5000);
        } else {
            Log.i("TAG", "此退出已执行");
//            myAsyncTask.cancel(true);
            MobclickAgent.onKillProcess(MainActivity.instance);
            for (Activity activity : MyApplication.list) {
                if (activity != null)
                    activity.finish();
            }
            System.exit(0);

        }
    }

    private void setWebs() {


        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        //支持缩放
        webSettings.setBuiltInZoomControls(true);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启缓存数据库功能set
        webSettings.setDatabaseEnabled(true);
        webSettings.setUseWideViewPort(true);
        //设置缓存模式

        //设置缓存路径
//        webSettings.setAppCachePath(appCachePath+"/clear");
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        //开启缓存功能
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        // Set cache size to 2 mb by default. should be more than enough
        webSettings.setAppCacheMaxSize(1024 * 1024 * 1);
        // This next one is crazy. It's the DEFAULT location for your app's cache
        // But it didn't work for me without this line.
        // UPDATE: no hardcoded path. Thanks to Kevin Hawkins

        webView.loadData("text/html", "utf-8", "utf-8");

        webView.addJavascriptInterface(new JavaScriptCallSon(), "JavaScriptCall");

        webView.setWebChromeClient(new MyWebChromeClient());

        webView.setWebViewClient(new MyOwnWebViewClient());


        webView.loadUrl(
                GloableUtils.HTTPURL
//                "http://www.baidu.com"
//                "http://192.168.0.110:8080/px-rest/kd/"
        );
    }

    //此类用于和js交互
    class JavaScriptCallSon implements JavaScriptCall {
        //进入选择图片页面
        //此方法选择可裁剪的图片
        @JavascriptInterface
        public void selectHeadPic() {
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            } else {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
            }
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, GloableUtils.RESULT_PICK_PHOTO_NORMAL);
        }

        //此方法不需要裁剪,进行多张图片选择，不启动自带应用，第三方组件
        @JavascriptInterface
        public void selectImgPic() {
//            Intent intent = null;
//            if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.KITKAT) {
//                intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//            }else{
//                intent = new Intent(Intent.ACTION_GET_CONTENT);
//            }
//            intent.setType("image/*");
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            startActivityForResult(intent, GloableUtils.CHOOSE_PICTURE_ONLY);
            startanotherApplication();
        }


        //调用此方法取消提示


        //js调用此方法将JessionId传过来将其保存
        @JavascriptInterface
        public void jsessionToPhone(String jessionID) throws UnsupportedEncodingException, JSONException {

            JESSIONID = jessionID;
            if(JESSIONID.equals("")){
                //如果为空，注销用户
                hideBottomAfaterCancle();
            }
            //当jessionid和设备号都不为空时来推送消息
            Log.i("TAG", "jessionid随时都在发送");
            DealWithPushMessage.dealPushMessage();
            if(GloableUtils.IS_NEED_AIAIN_START_ANIMATION.equals("false")){
                GloableUtils.IS_NEED_AIAIN_START_ANIMATION = "";
                return;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AnimationUtils.startMyAnimation(0, 0, 100, 0, radioGroup);
                    AnimationUtils.startMyAnimation(0,0,100,0,tv_line);
                }
            });
            Log.i("TAG", "jessionid已经穿古来了，当前时间为" + System.currentTimeMillis());
            //调用pushMessageToServer方法将渠道号和编号传到服务器
        }
        //此方法选择图片并压缩，不需裁剪
        @JavascriptInterface
        public void hideLoadingDialog() {
            try {
                hideText();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);

            }
        }

        //此方法在js回退到底之后调用来退出程序
        @JavascriptInterface
        public void finishProject() {

            finishAll();
        }

        @JavascriptInterface
        public void getPicUrlFromJs(String url) {
            myurl = url;
        }

        //js调用此方法拨打电话
//        @JavascriptInterface
//        public void callPhone(String tel){
//            MainActivity.this.startActivity(new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tel)));
//        }


    }

    private void startanotherApplication() {

        startActivity(new Intent(this, me.nereo.multiimageselector.MainActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
//        PushManager.startWork(this,
//                PushConstants.LOGIN_TYPE_API_KEY,
//                "El4au0Glwr7Xt8sPgZFg2UP7");
//        myAsyncTask = new MyAsyncTask();
//        this.myAsyncTask.execute();
        //注册反馈
//        FeedbackManager fm = FeedbackManager.getInstance(this);
//        fm.register(GloableUtils.WENJIE_BAIDU_API_KEY);
    }

    @Override
    protected void onDestroy() {

        MyPushMessageReceiver.f = true;
        unregisterReceiver(imageBroadcastReceiver);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    //在注销之后隐藏控件。
    public void hideBottomAfaterCancle() throws UnsupportedEncodingException, JSONException {
        MainActivity.handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("TAG", "注销之后隐藏控件了");
                AnimationUtils.hideRadioSlowy(MainActivity.instance.radioGroup, 1000);
                AnimationUtils.hideView(MainActivity.instance.tv_line);
            }
        });
        GloableUtils.IS_NEED_AIAIN_START_ANIMATION = "false";
//        GloableUtils.PUSH_STATE=2;
//        new PushMessage(asyncHttpClient).pushMessageToServer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Cursor cursor = null;
        try {
            if (requestCode == GloableUtils.RESULT_PICK_PHOTO_NORMAL && data != null) {
                String filePath = null;
                Uri uri = data.getData();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    filePath = BitmapUtils.getPath(uri);
                } else {
                    String[] cloums = {MediaStore.Images.Media.DATA};
                    cursor = getContentResolver().query(uri, cloums, null, null, null);
                    cursor.moveToFirst();
                    filePath = cursor.getString(0);
                }
                Log.i("TAG", "打印一下文件的路径" + filePath);
                BitmapUtils.cropImageUri(filePath);
            }
        } finally {

            if (cursor != null)
                cursor.close();
            System.gc();

        }
        if (requestCode == GloableUtils.CROP_A_PICTURE) {
            Log.i("TAG", "打印问题是不是出在data身上");
//
            Bitmap bitmap = null;
            try {
                String path = "/mnt/sdcard/temp.jpg";
                bitmap = BitmapUtils.compressPictureFromFile(path, bitmap);
                String pictureBytes = ParseUtils.getBase64FromBitmap(bitmap);
                pictureBytes =
                        "data:image/png;base64," + pictureBytes;
                Log.i("if", "打印base" + pictureBytes);
                Log.i("TAG", "返回了图片信息");
                webView.loadUrl("javascript:G_jsCallBack.selectHeadPic_callback('" + pictureBytes + "')");
            } catch (Exception e) {
                ExUtil.e(e);
            } finally {
                BitmapUtils.recyleBitmap(bitmap);
            }
        }
        if (requestCode == GloableUtils.CHOOSE_PICTURE_ONLY && data != null) {
            Bitmap bitmap = null;
            try {
                Uri uri = data.getData();
                String storagePath = BitmapUtils.getPath(uri);

                getImageBase(storagePath);
//                     base = "data:image/png;base64," + base;
//                     Log.i("if", "打印bsdfs" + base);
                //选择无裁剪图片成功后上传base64字符串
                //TODO
//                     webView.loadUrl("javascript:G_jsCallBack.selectPic_callback('" + base + "')");

            } finally {
                BitmapUtils.recyleBitmap(bitmap);
            }
        }
    }

    private String getImageBase(String imagePath) {
        Bitmap bitmap = null;
        String base = null;
//        SoftReference<Bitmap> softReference = null;
        try {
//            BitmapFactory.decodeFile()
            bitmap = BitmapUtils.compressPictureFromFile(imagePath, bitmap);
//            softReference = new SoftReference<Bitmap>(bitmap);
            base = ParseUtils.getBase64FromBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
//          softReference.clear();
            bitmap.recycle();
        }

        return base;
    }


    //获取图片路径的地址
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButton1:

                webView.loadUrl("javascript:menu_dohome()");
                break;
            case R.id.radioButton2:

                //通讯录点击按钮
                //获取屏幕高度与宽度
//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
                webView.loadUrl("javascript:G_jsCallBack.QueueTeacher()");
                break;
            case R.id.radioButton3:
                //即使消息
                //来了消息之后，图标一直点亮，不重复绘制，
                //当点击消息按钮之后图标消失，需要重新绘制。
                //每次消息来了时，用布尔值判断，true则点亮，false则消失，
                //每次点亮之后都设置为false，下次判断，如果是消失，则发送广播修改值，
                MyRadioButton.isMessage = false;
                radionbtown.canvasAgain();
                webView.loadUrl("javascript:G_jsCallBack.queryMyTimely_myList()");
                break;
            case R.id.radioButton4:
                //点击此按钮注销用户，并返回到登陆页面
                //回调javaScript方法
                //设置按钮。点击之后启动设置页面

                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    // 注入js函数监听
//        private void addImageClickListner() {
//            Log.i("TAG","遍历添加已完毕");
//                  // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
//                   webView.loadUrl("javascript:(function(){" +
//                           "var objs = document.getElementsByTagName(\"img\"); " +
//                                   "for(var i=0;i<objs.length;i++)  " +
//                           "{"
//                          + "    objs[i].onclick=function()  " +
//                          "    {  "
//                           + "        window.JavaScriptCall.getPicUrlFromJs(this.src);  " +
//                          "    }  " +
//                          "}" +
//                         "})()");
//
//                  webView.loadUrl("javascript:(function(){" +
//                          "" +"var phone = document.getElementById(" +
//                                  //TODO需要传入的标签id
//                                  "" +
//                                  "); "+
//                                "phone.onclick=function()  "+
//                              "  {" +
//                                 "    window.JavaScriptCall.callPhone()"+
//
//                          "})");
//               }
    public void clear() {
        WebStorage webStorage = WebStorage.getInstance();
        webStorage.deleteAllData();
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearHistory();
        webView.clearSslPreferences();
        webView.clearMatches();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("info", "看看方法横竖品几次");
        super.onConfigurationChanged(newConfig);
    }

    class ImageBroadcastReceiver extends BroadcastReceiver {

        String base;


        @Override
        public void onReceive(Context context, Intent intent) {
            animationRl.setVisibility(View.VISIBLE);
            tv_permit.setText("图片上传中.....");
            tv_permit.setVisibility(View.VISIBLE);
            //得到图片地址的集合
            final List<String> imageList = (List) intent.getSerializableExtra("imageList");
            for (String path : imageList) {
                Log.i("info", "bitmap了几次0000000");
                picbase = getImageBase(path);
                Log.i("TAG", "打印base字符串ssssssssss" + picbase);
                picbase = "data:image/png;base64," + picbase;
                webView.loadUrl("javascript:G_jsCallBack.selectPic_callback('" + picbase + "')");
            }

            animationRl.setVisibility(View.GONE);
            tv_permit.setVisibility(View.GONE);
        }
    }
}

