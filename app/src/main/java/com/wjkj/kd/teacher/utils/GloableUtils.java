package com.wjkj.kd.teacher.utils;
import android.net.Uri;
import android.os.Environment;

import com.wjkj.kd.teacher.MyApplication;

import java.io.File;

public class GloableUtils {
    public static String WENJIE_BAIDU_API_KEY = "4311ed70ee";
    public static String CHANNL_ID  = "wenjiehudongjiayuanlaoshiban";
    public static final String APPLICATION_JSON = "application/json";
    public static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";
    public static final int CROP_A_PICTURE = 10;
    public static final int CHOOSE_PICTURE_ONLY = 20;
    public static Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);
    //手机input file上传图片
    public static final int FILECHOOSER_RESULTCODE = 100;
    public static final int RESULT_PICK_PHOTO_NORMAL = 1;
    public static int PUSH_STATE = 0;
    public static final String CANCLE_USER = "javascript:G_jsCallBack.userinfo_logout()";
    public static String URL = "http://wapbaike.baidu.com/view/4850574.htm?sublemmaid=" +
            "15923552&adapt=1&fr=aladdin&target=_blank";
    //另一个服务器地址

//        public static String ServerURL = "http://120.24.61.97/px-rest/";

    //一个服务器地址

//        public static String ServerURL = "http://120.25.248.31/px-rest/";

    //测试服务器地址
//        public static final String ServerURL = "http://120.25.212.44/px-mobile/";

    //正式环境
    public static String ServerURL = "http://kd.wenjienet.com/px-rest/";

    //调试用，胡溪斌的地址
//    public static String ServerURL = "http://192.168.0.177:8080/px-rest/";

    //刘老大，调试用

//    public static String ServerURL = "http://192.168.0.108:8080/px-rest/";

    public static String InterfaceURL = ServerURL+"rest/";
    public static String HTTPURL =
//            "http://www.baidu.com";
//              "http://www.sina.com";
            ServerURL
//            "http://kd.wenjienet.com/px-rest/"
                    +"kd/index.html";
    public static String appCachePath = MyApplication.instance.getCacheDir().getAbsolutePath();
//            "http://120.25.248.31/px-rest/kd/index.html"

    public static String IS_NEED_AIAIN_START_ANIMATION = "";

    //获取最新的网页地址的url

    public static final String newUrl=InterfaceURL+"share/getKDWebUrl.json";

    public static final String parentPath = Environment.getExternalStorageDirectory() +
            File.separator + "CG" + File.separator;
    public static final String cachePath = parentPath + "cache";
}
