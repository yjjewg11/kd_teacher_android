package com.wjkj.kd.teacher.biz;


import com.loopj.android.http.AsyncHttpClient;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.utils.GloableUtils;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class DealWithPushMessage {

    public static AsyncHttpClient asyncHttpClient =  new AsyncHttpClient();

    public static void dealPushMessage() throws UnsupportedEncodingException, JSONException {
            if(MainActivity.instance.device_token==null&& GloableUtils.JESSIONID==null) return;

            new PushMessage(asyncHttpClient).pushMessageToServer();
    }

}
