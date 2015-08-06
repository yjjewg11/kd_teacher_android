package com.wjkj.kd.teacher.com.wjkj.kd.teacher.biz;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.umeng.analytics.MobclickAgent;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.MyApplication;
import com.wjkj.kd.teacher.R;
import com.wjkj.kd.teacher.VersionActivity;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class Menu {

    Button[] btnArray = new Button[7];
    Activity activity;
    SlidingMenu slidingMenu;
    public Button btPush;
    public Menu(Activity activity) {
        super();
        this.activity = activity;
        slidingMenu = new SlidingMenu(activity);
        slidingMenu.setMenu(R.layout.leftmenu);
        LinearLayout linearLayout = (LinearLayout)View.inflate(MyApplication.instance,R.layout.leftmenu,null);
        btPush = (Button)linearLayout.findViewById(R.id.btn_alter_message);
        slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        slidingMenu.setBehindOffset(screenWidth * 3 / 8);

        btnArray[0] = (Button) activity
                .findViewById(R.id.push_message);
        btnArray[1] = (Button) activity
                .findViewById(R.id.btn_version);
        btnArray[2] = (Button) activity
                .findViewById(R.id.btn_cancle);
        btnArray[3] = (Button) activity
                .findViewById(R.id.btn_finish);
        btnArray[4] = (Button) activity.findViewById(R.id.btn_alter_message);
        btnArray[5] = (Button) activity.findViewById(R.id.btn_alter_password);
        btnArray[6] = (Button) activity.findViewById(R.id.btn_leftmenu_setting);

        MyListener myListener = new MyListener();
        for (Button btn : btnArray) {
            btn.setOnClickListener(myListener);
        }

    }

    public void showMenu() {
        slidingMenu.showMenu();
    }

    class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            //如果R.id.*报错
            //用if
//			if (v.getId()==R.id.btn_leftmenu_allTopic)
//			{index=0;}

            try {
                switch (v.getId()) {
                    case R.id.push_message:
                        //调用一个方法将消息推送设置状态改变
                        changePushState();
                        break;
                    case R.id.btn_version:
                        //启动一个界面观察此程序的版本号
                        Intent intent = new Intent(activity, VersionActivity.class);
                        activity.startActivity(intent);
                        break;
                    case R.id.btn_cancle:
                        //调用js接口注销当前用户
                        Log.i("TAG", "注销用户");
                        if (activity instanceof MainActivity) {
                            MainActivity mainActivity = (MainActivity) activity;
                            mainActivity.webView.loadUrl(MainActivity.CANCLE_USER);
                            Log.i("TAG", "注销方法已执行");

                        }

                        break;
                    case R.id.btn_finish:
                        //退出当前程序
                        MobclickAgent.onKillProcess(MainActivity.instance);
                        for (Activity activity : MyApplication.list) {
                            if(activity!=null)
                            activity.finish();
                        }

                        MainActivity.instance.myAsyncTask.cancel(true);



                        System.exit(0);

                        break;
                    case R.id.btn_alter_message:
                        //调用js接口修改联系人信息
                        MainActivity.instance.webView.loadUrl("javascript:G_jsCallBack.user_info_update()");
                        break;
                    case R.id.btn_alter_password:
                        //调用js接口修联系人密码
                        MainActivity.instance.webView.loadUrl("javascript:G_jsCallBack.user_info_updatepassword()");
                        break;
                    case R.id.btn_leftmenu_setting:
                        //一个设置界面
                        //点击清楚缓存数据
//

//                    if(totalSize>2){

//                    }

//                    MainActivity.instance.webView.

                        //此方法执行清理
//                    File file = new File(MainActivity.instance.appCachePath+"/clear");
//                    Log.i("TAG", "打印一下文件的绝对路径" + file.getAbsolutePath());
//                    long size =  file.length();
//                    file.isDirectory();
//                    Log.i("TAG", "打印一下是否是目录" + file.isDirectory());
//                    Log.i("TAG", "打印一下是否是文件" + file.isFile());
//                    Log.i("TAG", "打印一下文件的长度" + size);
//                    Log.i("TAG","打印目录是否存在"+file.exists());
//                    Log.i("TAG","打印数据隐藏"+file.isHidden());
//                    Log.i("TAG","打印能不能读"+file.canRead());
//                    Log.i("TAG","打印能不能写"+file.canWrite());
                        MainActivity.instance.clear();
//                    MainActivity.instance.webView.c
//                    long f = file.getTotalSpace();
//                    long f2 = file.getUsableSpace();
//                    long f3 = file.getFreeSpace();
//                    Log.i("TAG", "打印文件的大小getTotalSpace==" + f + "   getUsableSpace===" + f2 + "    getFreeSpace====" + f3);


                        break;
                }
            }finally {
                slidingMenu.toggle();
            }

        }



    }



    //点击清楚缓存数据
    private void clearCookie() {
        try {
            File file = new File(MainActivity.instance.appCachePath,"clear");
            Log.i("TAG","打印此文件的路径"+file.getAbsolutePath());


                long f = file.getTotalSpace();
                long f2 = file.getUsableSpace();
                long f3 = file.getFreeSpace();
                Log.i("TAG", "打印文件的大小getTotalSpace==" + f + "   getUsableSpace===" + f2 + "    getFreeSpace====" + f3);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void changePushState(){
        if(MainActivity.PUSH_STATE==0){
            MainActivity.PUSH_STATE = 1;
            btPush.setText("推送已打开");
        }else{
            MainActivity.PUSH_STATE = 0;
            btPush.setText("推送已关闭");
        }

        pushMessage();
    }

    public void  pushMessage(){
        try {
            MainActivity.instance.pushMessageToServer();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
