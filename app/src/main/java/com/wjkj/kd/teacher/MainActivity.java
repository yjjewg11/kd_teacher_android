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
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.umeng.fb.FeedbackAgent;
import com.wjkj.kd.teacher.bean.ShareContent;
import com.wjkj.kd.teacher.biz.DealWithPushMessage;
import com.wjkj.kd.teacher.biz.MyAsyncTask;
import com.wjkj.kd.teacher.biz.MyOwnWebViewClient;
import com.wjkj.kd.teacher.biz.MyWebChromeClient;
import com.wjkj.kd.teacher.biz.PushMessage;
import com.wjkj.kd.teacher.biz.RegistUmengService;
import com.wjkj.kd.teacher.biz.SettingWebParams;
import com.wjkj.kd.teacher.interfaces.JavaScriptCall;
import com.wjkj.kd.teacher.receiver.ListenConntectStatesReceiver;
import com.wjkj.kd.teacher.utils.AnimationUtils;
import com.wjkj.kd.teacher.utils.BitmapUtils;
import com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.utils.FinishUtils;
import com.wjkj.kd.teacher.utils.GloableUtils;
import com.wjkj.kd.teacher.utils.ParseUtils;
import com.wjkj.kd.teacher.utils.ShareUtils;
import com.wjkj.kd.teacher.utils.Util;
import com.wjkj.kd.teacher.views.MyRadioButton;
import com.xiaomi.market.sdk.XiaomiUpdateAgent;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
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
    private String loginName;
    private String callbackGlobal;
    private String maxConutGlobal;
    private String qualityGlobal;

    public String getLoginName() {
        return loginName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //获得屏幕的尺寸
//        long max =  Runtime.getRuntime().maxMemory()/1024;
//        map = new LruCache<>((int)(max/4));
        XiaomiUpdateAgent.update(this);
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

    }

    public String webUrl = "";
    private void setWebs() {
        getWebUrl();
        new SettingWebParams().setWebs(webView);
        //取消webView滑动时边框
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new JavaScriptCallSon(), "JavaScriptCall");
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyOwnWebViewClient());

//        设置cookie
        webView.loadUrl(webUrl);

//        //调试地址
//        webView.loadUrl(GloableUtils.TestServerURL);
    }


//    private void setCookie() {
//        String cook = "JSESSIONID=OJh2-q59qg-Ij7D8KPc+McFH.undefined; SERVERID=" +
//                "256fdebb4452ae158915cd23a915b948|" +
//                "1442041243|1442037522";
//        CookieManager cookieManager = CookieManager.getInstance();
//
//        cookieManager.setCookie(webUrl, cook);
//
////        cookieManager.
//    }

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
        webView.loadUrl("javascript:G_jsCallBack.QueuedoBackFN();");
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
            isFirstSendMessage();

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
                    ShareUtils.showShareDialog(MainActivity.this,tv_line,title,content,pathUrl,links);
                }
            });
        }

        @JavascriptInterface
        public void openNewWindowUrl(final String title,final String content, final String pathurl, final String httpurl){
            //启动另外的界面浏览文章
            Intent intent = new Intent(MainActivity.this,LoadUrlActivity.class);
            intent.putExtra("shareContent",new ShareContent(title,content,pathurl,httpurl));
            startActivity(intent);
        }

        @JavascriptInterface
        public void jsessionToPhoneTel(String name){
            loginName = name;
        }
        @JavascriptInterface
        public void selectImgForCallBack(String paramString1, String paramString2, String paramString3) {
            MainActivity.this.callbackGlobal = paramString1;
            MainActivity.this.maxConutGlobal = paramString2;
            MainActivity.this.qualityGlobal = paramString3;
            MainActivity.this.startanotherApplication();
        }

        @JavascriptInterface
        public void selectImgPic() {
            selectImgForCallBack("", "", "");
        }
    }

    public void isFirstSendMessage() throws UnsupportedEncodingException, JSONException {
        if(!sp.getBoolean("isSend",false)){
            DealWithPushMessage.dealPushMessage();
            editor.putBoolean("isSend",true);
            editor.commit();
        }
    }

    private void startanotherApplication() {
        Intent localIntent = new Intent(this, me.nereo.multiimageselector.MainActivity.class);
        localIntent.putExtra("maxCount", this.maxConutGlobal);
        startActivity(localIntent);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        instance = null;
        unregisterReceiver(imageBroadcastReceiver);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    //在注销之后隐藏控件。
    public void hideBottomAfaterCancle() throws UnsupportedEncodingException, JSONException {
        MainActivity.handler.post(new Runnable() {
            @Override
            public void run() {
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
                bitmap = BitmapUtils.compressPictureFromFile(path);
                String pictureBytes = ParseUtils.getBase64FromBitmap(bitmap);
                pictureBytes =
                        "data:image/png;base64," + pictureBytes;
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

        if (requestCode == GloableUtils.FILECHOOSER_RESULTCODE) {
            if (null == myUploadMsg)
                return;
            Uri result = data == null || resultCode != RESULT_OK ? null
                    : data.getData();
            myUploadMsg.onReceiveValue(result);
            myUploadMsg = null;
        }
    }

    private HashMap<String,SoftReference<Bitmap>> map = new HashMap<>();
    //使用框架传入bitmap并且上传
    private String getImageBase(String imagePath) {
        Bitmap bitmap = null;
        String base = null;
        try {
            Log.i("TAG","打印hashMap"+map);
            //先从缓存中查找，如果没有加载本地图片
            if(map.containsKey(imagePath) && map.get(imagePath)!= null
                    && map.get(imagePath).get() != null && !map.get(imagePath).get().isRecycled()){
                    bitmap = map.get(imagePath).get();
            }else{
                bitmap = putBitmap(imagePath);
            }
            base = ParseUtils.getBase64FromBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            bitmap.recycle();
            System.gc();
        }

        return base;
    }

    private Bitmap putBitmap(String imagePath) {
        Bitmap bitmap = null;
        bitmap = BitmapUtils.compressPictureFromFile(imagePath);
        map.put(imagePath,new SoftReference<Bitmap>(bitmap));
        return bitmap;
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

    public void clear() {
//        WebStorage webStorage = WebStorage.getInstance();
//        webStorage.deleteAllData();
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
        webView.clearCache(true);
        webView.clearHistory();
        clearCache(getCacheDir().toString());
    }

    private void clearCache(String cachePath) {
        File file = new File(cachePath);


        //遍历所有缓存目录并进行删除。

        Util.setDoMyThing(new Util.DoMything() {
            @Override
            public boolean doOwnThing(File file) {
                return file.delete();
            }
        });
        Util.deleteDir(file);


    }
    long allSize;


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    class ImageBroadcastReceiver extends BroadcastReceiver {

        String base;


        @Override
        public void onReceive(Context context, final Intent intent) {
            animationRl.setVisibility(View.VISIBLE);
            tv_permit.setText("图片上传中.....");
            tv_permit.setVisibility(View.VISIBLE);
            //得到图片地址的集合
            new Thread(){
                @Override
                public void run() {
                    final List<String> imageList = (List) intent.getSerializableExtra("imageList");
                    for (String path : imageList) {
                        picbase = getImageBase(path);
                        picbase = "data:image/png;base64," + picbase;
                        //获得运行时内存
                        handler.post(new Runnable() {
                            @Override
                            public void run(){
                                if (!Util.stringIsNull(MainActivity.this.callbackGlobal)){
                                    MainActivity.this.webView.loadUrl("javascript:" + MainActivity.this.callbackGlobal + "('" + MainActivity.this.picbase + "','"+imageList.size()+"')");
                                    return;
                                }
                                MainActivity.this.webView.loadUrl("javascript:G_jsCallBack.selectPic_callback('" + MainActivity.this.picbase + "')");
                            }
                        });

                    }
                }
            }.start();
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

