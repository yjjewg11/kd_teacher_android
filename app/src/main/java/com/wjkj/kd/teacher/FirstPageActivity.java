package com.wjkj.kd.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class FirstPageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        //有网络情况下启动主页
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    startActivity(new Intent(FirstPageActivity.this, MainActivity.class));
                    finish();
            }
        }, 2000);
    }
}
