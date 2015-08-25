package com.wjkj.kd.teacher;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
//import com.wjkj.kd.teacher.biz.Menu;

public abstract class BaseActivity extends Activity implements View.OnClickListener

{
//    private Menu menu;

    public static Activity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.list.add(this);
        PushAgent.getInstance(this).onAppStart();
        instance = this;
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);


            MobclickAgent.onResume(this);






    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);

        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
