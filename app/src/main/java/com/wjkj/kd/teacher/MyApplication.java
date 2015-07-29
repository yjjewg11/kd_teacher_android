package com.wjkj.kd.teacher;

import android.app.Application;
import android.content.Context;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;


public class MyApplication extends Application{
    public static Context instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        StatService.setAppKey("4311ed70ee");
        StatService.setAppChannel(this, "null", false);
//        El4au0Glwr7Xt8sPgZFg2UP7
//        启动推送服务
        PushManager.startWork(this,
                PushConstants.LOGIN_TYPE_API_KEY,
                "4QTEeP8BGFVCrb21fYplColm");
    }
}
