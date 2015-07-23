package com.wjkj.kd.teacher.com.wjkj.kd.teacher.receiver;

import android.content.Context;

import com.baidu.android.pushservice.PushMessageReceiver;

import java.util.List;

public class MyPushMessageReceiver extends PushMessageReceiver{

    @Override
    public void onBind(Context context, int i, String s, String s2, String s3, String s4) {

    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> strings, List<String> strings2, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> strings, List<String> strings2, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> strings, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s2) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s2, String s3) {

    }

    @Override
    public void onNotificationArrived(Context context, String s, String s2, String s3) {

    }
}
