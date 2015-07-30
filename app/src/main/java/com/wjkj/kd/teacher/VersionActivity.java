package com.wjkj.kd.teacher;

import android.app.Activity;
import android.os.Bundle;

import com.wjkj.kd.teacher.com.wjkj.kd.teacher.biz.Menu;


public class VersionActivity extends Activity {

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        menu = new Menu(this);

    }

}
