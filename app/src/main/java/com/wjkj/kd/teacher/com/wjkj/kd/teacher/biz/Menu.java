package com.wjkj.kd.teacher.com.wjkj.kd.teacher.biz;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.MyApplication;
import com.wjkj.kd.teacher.R;
import com.wjkj.kd.teacher.VersionActivity;
public class Menu {
    Button[] btnArray = new Button[7];
    Activity activity;
    SlidingMenu slidingMenu;
    public Menu(Activity activity) {
        super();
        this.activity = activity;
        slidingMenu = new SlidingMenu(activity);
        slidingMenu.setMenu(R.layout.leftmenu);
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

            switch (v.getId()) {
                case R.id.push_message:
                    //调用一个方法将消息推送设置状态改变
                    changePushState();
                    break;
                case R.id.btn_version:
                    //启动一个界面观察此程序的版本号
                    Intent intent = new Intent(activity,VersionActivity.class);
                    activity.startActivity(intent);
                    break;
                case R.id.btn_cancle:
                    //调用js接口注销当前用户
                   if(activity instanceof MainActivity){
                       MainActivity mainActivity = (MainActivity)activity;
                       mainActivity.webView.loadUrl(MainActivity.CANCLE_USER);
                       slidingMenu.toggle();
                   }

                    break;
                case R.id.btn_finish:
                    //退出当前程序
                    for(Activity activity : MyApplication.list){
                        activity.finish();
                    }
                    System.exit(0);

                    break;
                case R.id.btn_alter_message:
                    //调用js接口修改联系人信息
                    break;
                case R.id.btn_alter_password:
                   //调用js接口修联系人密码
                    break;
                case R.id.btn_leftmenu_setting:

                    //一个设置界面
                    break;
            }

        }

    }

    private void changePushState() {
        if(MainActivity.PUSH_STATE==0){
            MainActivity.PUSH_STATE = 1;
        }else{
            MainActivity.PUSH_STATE = 0;
        }
    }
}
