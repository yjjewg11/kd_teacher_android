package com.wjkj.kd.teacher.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.MyApplication;
import com.wjkj.kd.teacher.utils.HttpUtils;
import com.wjkj.kd.teacher.views.ConntectErrorActivity;

public class ListenConntectStatesReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

            //此接受器用于接受网络连接状态的该变
        if(HttpUtils.getConntectStates()){
            Intent intentOld = new Intent(MyApplication.instance,MainActivity.class);
            intentOld.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            MyApplication.instance.startActivity(intentOld);
        }else{
            //如果网络状态没有链接，则启动错误页面
            MainActivity.instance.startActivity(new Intent(MainActivity.instance, ConntectErrorActivity.class));
        }

    }
}
