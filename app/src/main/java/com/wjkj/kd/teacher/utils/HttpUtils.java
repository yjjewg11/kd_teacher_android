package com.wjkj.kd.teacher.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.wjkj.kd.teacher.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpUtils {
    //此静态方法解析json字符串
    public static void pullJson(JSONObject jsonObject) throws JSONException {
        JSONObject json = jsonObject.getJSONObject("ResMsg");
        if("success".equals(json.getString("status"))){
//                 ToastUtils.showMessage(json.getString("message"));
            Log.i("TAG", "打印返回成功之后的信息      " + json.getString("message"));

            //指明当成功上传之后该如何做
            //推送消息的渠道首次上传成功后，下次就不需要再上传了，所以关闭异步任务
//            MainActivity.instance.myAsyncTask.cancel(true);
        }else{
            //失败则显示原因
//            ToastUtils.showMessage(json.getString("message"));
            Log.i("TAG","看看返回的json字符串"+json.getString("message"));
        }
    }


    //判断网络连接状态
    public static boolean getConntectStates() {
        ConnectivityManager c = (ConnectivityManager) MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (c == null) return false;
        NetworkInfo[] info = c.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                                         Log.i("NetWorkState", "Availabel");
                    return true;
                }
            }
        }
        return false;
    }
}
