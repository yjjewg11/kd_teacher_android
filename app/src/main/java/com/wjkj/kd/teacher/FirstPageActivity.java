package com.wjkj.kd.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wjkj.kd.teacher.utils.HttpUtils;
import com.wjkj.kd.teacher.views.ConntectErrorActivity;

public class FirstPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        //有网络情况下启动主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (HttpUtils.getConntectStates()) {
                    startActivity(new Intent(FirstPageActivity.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(FirstPageActivity.this, ConntectErrorActivity.class));
                }
            }
        }, 2000);


    }



}
