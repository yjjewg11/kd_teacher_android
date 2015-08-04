package com.wjkj.kd.teacher;

import android.app.Activity;
import android.app.Application;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;


public class MyApplication extends Application{
    public static MyApplication instance;
    //此集合用于存储activity对象
    public static ArrayList<Activity> list = new ArrayList();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        StatService.setAppKey("4311ed70ee");
        StatService.setAppChannel(this, "null", false);
//        El4au0Glwr7Xt8sPgZFg2UP7
//        启动推送服务

    }
}
