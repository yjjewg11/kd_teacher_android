package com.wjkj.kd.teacher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.umeng.fb.FeedbackAgent;
import com.wjkj.kd.teacher.biz.DealWithPushMessage;
import com.wjkj.kd.teacher.biz.MyAsyncTask;
import com.wjkj.kd.teacher.biz.MyOwnWebViewClient;
import com.wjkj.kd.teacher.biz.MyWebChromeClient;
import com.wjkj.kd.teacher.biz.PushMessage;
import com.wjkj.kd.teacher.biz.RegistUmengService;
import com.wjkj.kd.teacher.biz.SettingWebParams;
import com.wjkj.kd.teacher.interfaces.JavaScriptCall;
import com.wjkj.kd.teacher.receiver.ListenConntectStatesReceiver;
import com.wjkj.kd.teacher.receiver.MyPushMessageReceiver;
import com.wjkj.kd.teacher.utils.AnimationUtils;
import com.wjkj.kd.teacher.utils.BitmapUtils;
import com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.utils.FinishUtils;
import com.wjkj.kd.teacher.utils.GloableUtils;
import com.wjkj.kd.teacher.utils.ParseUtils;
import com.wjkj.kd.teacher.utils.ShareUtils;
import com.wjkj.kd.teacher.utils.Util;
import com.wjkj.kd.teacher.views.MyRadioButton;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

//import com.wjkj.kd.teacher.biz.Menu;

public class MainActivity extends BaseActivity {
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    public WebView webView;
    public RadioGroup radioGroup;
    public static MainActivity instance;
    public MyAsyncTask myAsyncTask;
    public ValueCallback<Uri> myUploadMsg;
    public TextView tv_permit;
    public static Handler handler;
    public WebSettings webSettings;
    public String myurl;
    public FeedbackAgent agent;
    public String device_token;
    public PushMessage pushMessage;
    public TextView tv_line;
    public MyRadioButton radionbtown;
    private ImageView imageLoading;
    public RelativeLayout animationRl;
    private ImageBroadcastReceiver imageBroadcastReceiver;
    private String picbase;
    public int width;
    public int height;
    public String JESSIONID;
    private ListenConntectStatesReceiver receiver;
    private RegistUmengService registUmengService;
    public SharedPreferences sp;
    public SharedPreferences.Editor editor;
    public String httpPicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //获得屏幕的尺寸

        getWidthSize();
        setContentView(R.layout.activity_mymain);
        instance = this;
        setViews();
        register();
        try {
            //注册友盟的几种服务

            registUmengService = new RegistUmengService(this);
            registUmengService.registService();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //禁用侧滑菜单
//              menu = new Menu(this);

    }

    private void register() {
        //注册接受图片地址的广播
        imageBroadcastReceiver = new ImageBroadcastReceiver();
        IntentFilter filter = new IntentFilter(me.nereo.multiimageselector.MainActivity.RESOLVE_IMAGE);
        registerReceiver(imageBroadcastReceiver, filter);

        //注册网络监听状态广播
        receiver = new ListenConntectStatesReceiver();
        IntentFilter confilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, confilter);
    }

    private void getWidthSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Log.i("TAG", "打印屏幕的宽度" + width);
        if (width == 480) {
            MyRadioButton.widthSize = 90;
        } else if (width == 720) {
            MyRadioButton.widthSize = 120;
        } else if (width == 1080) {
            MyRadioButton.widthSize = 160;
        }
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
        Log.i("TAG", "handler刚刚初始化完毕,当前时间为" + System.currentTimeMillis());


    }

    public String webUrl = "";
    private void setWebs() {
        getWebUrl();
        Log.i("TAG", "打印weburl的值" + webUrl);
        new SettingWebParams().setWebs(webView);
        //取消webView滑动时边框
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new JavaScriptCallSon(), "JavaScriptCall");
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyOwnWebViewClient());

//        设置cookie
        webView.loadUrl(webUrl);

//        //调试地址
//        webView.loadUrl(GloableUtils.HTTPURL);

    }


    private void setCookie() {
        String cook = "JSESSIONID=OJh2-q59qg-Ij7D8KPc+McFH.undefined; SERVERID=" +
                "256fdebb4452ae158915cd23a915b948|" +
                "1442041243|1442037522";
        CookieManager cookieManager = CookieManager.getInstance();

        cookieManager.setCookie(webUrl, cook);

//        cookieManager.
    }

    private void getWebUrl() {
        sp = getSharedPreferences("webUrl", 0);
        editor =  sp.edit();
        //先从网络获取，如果获取到就加载这个，这个一定是最新的
        if(!TextUtils.isEmpty(MyApplication.instance.justUrl)){
              webUrl = MyApplication.instance.justUrl;
              editor.putString("url",MyApplication.instance.justUrl);
              editor.commit();
            return;
        }
        //如果网络没有，则从文件获取，如果没有保存过，则使用默认加载的
        if(!TextUtils.isEmpty(sp.getString("url",""))){
                webUrl = sp.getString("url","");
                return;
        }
        webUrl = GloableUtils.HTTPURL;
        editor.putString("url", GloableUtils.HTTPURL);
        editor.commit();
    }

    private RotateAnimation getDialogAnimation() {
        RotateAnimation animation = new RotateAnimation(0f, 358.0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(400);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);
        return animation;
    }


    @Override
    public void onBackPressed() {
        if (tv_permit.getVisibility() == View.VISIBLE
//                && progressBar.getVisibility() == View.VISIBLE
                ) {
            finishAll();
        }
        webView.loadUrl("javascript:G_jsCallBack.QueuedoBackFN()");
    }

    private void finishAll() {
        FinishUtils.finishProjectAgain(MyApplication.list);
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
            startanotherApplication();
        }
        //调用此方法取消提示
        //js调用此方法将JessionId传过来将其保存
        @JavascriptInterface
        public void jsessionToPhone(String jessionID)
                throws UnsupportedEncodingException, JSONException {

            JESSIONID = jessionID;
            if(JESSIONID.equals("")){
                //如果为空，注销用户
                hideBottomAfaterCancle();

            }
            //当jessionid和设备号都不为空时来推送消息
            Log.i("TAG", "jessionid随时都在发送");
            if(!sp.getBoolean("isSend",false)){
                DealWithPushMessage.dealPushMessage();
                editor.putBoolean("isSend",true);
                editor.commit();
            }

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

        /*
        * 网页回掉此接口，传递分享内容进行分享
        * **/

        @JavascriptInterface
        public void setShareContent(final String title,final String content, final String pathUrl, final String links){
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    Log.i("TAG","打印穿过来的标题"+title);
//                    Log.i("TAG","打印传过来的内容"+content);
//                    Log.i("TAG","打印闯过来的图片地址"+pathUrl);
//                    Log.i("TAG","打印传过来的链接地址"+links);
                    ShareUtils.showShareDialog(MainActivity.this,tv_line,title,content,pathUrl,links);
                }
            });
        }

    }

    private void startanotherApplication() {

        startActivity(new Intent(this, me.nereo.multiimageselector.MainActivity.class));
    }
    @Override
    protected void onStart() {
        super.onStart();
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
            System.gc();
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
//                registUmengService.setShareContent("你好今天有肉吃了","/storage/emulated/0/DCIM/Camera/1437468583135.jpg","http://www.baidu.com");
////                查看社会化分享
//                registUmengService.mController.openShare(this,false);


                //查看自定义分享

//                ShareUtils.showShareDialog(this,tv_line,"你好，这是标题,","这有什么内容",
//                        "","http://www.baidu.com"
//                        );

//                share();
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
//        WebStorage webStorage = WebStorage.getInstance();
//        webStorage.deleteAllData();
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
        webView.clearCache(true);
        webView.clearHistory();
        Log.i("TAG", "缓存路径" + getCacheDir().toString());
        clearCache(getCacheDir().toString());
    }

    private void clearCache(String cachePath) {
        File file = new File(cachePath);


        //遍历所有缓存目录并进行删除。

        Util.setDoMyThing(new Util.DoMything() {
            @Override
            public boolean doOwnThing(File file) {
                Log.i("TAG","打印目录的名字"+file.getName());
                return file.delete();
            }
        });
        Util.deleteDir(file);


    }
    long allSize;


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
            Log.i("TAG","打印图片的地址"+imageList);
            for (String path : imageList) {
                Log.i("info", "bitmap了几次0000000");
                picbase = getImageBase(path);
                picbase = "data:image/png;base64," + picbase;
                //获得运行时内存



                webView.loadUrl("javascript:G_jsCallBack.selectPic_callback('" + picbase + "')");
            }
            animationRl.setVisibility(View.GONE);
            tv_permit.setVisibility(View.GONE);
        }
    }


//
//    private boolean setWebCookie() {
//        Cookie sessionCookie = JlxCustomerHttpClient.getCookie();
//        CookieSyncManager.createInstance(this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        if (sessionCookie != null) {
//            if (JlxApp.IS_LOGIN) {
//                String cookieString = sessionCookie.getName() + "="
//                        + sessionCookie.getValue() + "; domain="
//                        + sessionCookie.getDomain();
//                cookieManager.setCookie(webUrl,cookieString);
//                return true;
//            } else {
//                cookieManager.removeSessionCookie();
//            }
//            CookieSyncManager.getInstance().sync();
//        }
//        return false;
//    }

}

