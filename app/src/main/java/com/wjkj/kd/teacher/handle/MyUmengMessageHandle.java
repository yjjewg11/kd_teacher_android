package com.wjkj.kd.teacher.handle;


import android.app.Notification;
import android.content.Context;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.views.MyRadioButton;

public class MyUmengMessageHandle extends UmengMessageHandler{


        @Override
        public Notification getNotification(Context context, UMessage uMessage) {
            MyRadioButton.isMessage = true;
            if(MainActivity.instance.radionbtown!=null)
                MainActivity.instance.radionbtown.canvasAgain();
            return super.getNotification(context, uMessage);
        }

        @Override
        public void dealWithNotificationMessage(Context context, UMessage uMessage) {
            super.dealWithNotificationMessage(context, uMessage);

    }

}
