package com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class HttpUtils {
    //此静态方法解析json字符串
    public static void pullJson(JSONObject jsonObject) throws JSONException {
        JSONObject json = jsonObject.getJSONObject("ResMsg");
        if("success".equals(json.getString("status"))){
                 ToastUtils.showMessage(json.getString("message"));
            Log.i("TAG","打印返回成功之后的信息      "+json.getString("message"));
            //指明当成功上传之后该如何做
        }else{
            //失败则显示原因
            ToastUtils.showMessage(json.getString("message"));
            Log.i("TAG","看看返回的json字符串"+json.getString("message"));
        }

    }
}
