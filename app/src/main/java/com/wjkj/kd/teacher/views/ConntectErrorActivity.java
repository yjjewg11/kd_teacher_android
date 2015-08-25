package com.wjkj.kd.teacher.views;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wjkj.kd.teacher.BaseActivity;
import com.wjkj.kd.teacher.R;

public class ConntectErrorActivity extends BaseActivity {

    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conntect_error);
        setViews();
    }

    private void setViews() {
        bt = (Button)findViewById(R.id.bt_open_conntect);
        bt.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_open_conntect:
                //打开网络设置界面
                openConntectActivity();
                break;
        }
    }

    private void openConntectActivity() {
        Intent intent=null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if(android.os.Build.VERSION.SDK_INT>10){
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        }else{
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        startActivity(intent);
    }
}
