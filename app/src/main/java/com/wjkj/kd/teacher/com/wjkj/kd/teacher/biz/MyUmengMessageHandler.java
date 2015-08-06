package com.wjkj.kd.teacher.com.wjkj.kd.teacher.biz;

import android.content.Context;

import com.umeng.fb.push.FBMessage;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

public class MyUmengMessageHandler extends UmengMessageHandler {
    @Override
    public void dealWithCustomMessage(Context context, UMessage msg) {
        if (FeedbackPush.getInstance(context).dealFBMessage(new FBMessage(msg.custom))) {
            return;
        }
    }
}
