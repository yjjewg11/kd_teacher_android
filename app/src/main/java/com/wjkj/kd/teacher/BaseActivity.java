package com.wjkj.kd.teacher;


import android.app.Activity;
import android.view.View;

import com.baidu.mobstat.StatService;

public class BaseActivity extends Activity implements View.OnClickListener{

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause (this);
    }
}
