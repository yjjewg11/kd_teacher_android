package com.wjkj.kd.teacher;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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

        MobclickAgent.onResume(this);






    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
