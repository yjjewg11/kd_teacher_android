package com.wjkj.kd.teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wjkj.kd.teacher.biz.Menu;


public class VersionActivity extends BaseActivity {

    private Menu menu;

    public ImageView ivservison;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        menu = new Menu(this);
        ivservison = (ImageView)findViewById(R.id.imageView_servison);
        ImageLoader.getInstance().displayImage(MainActivity.instance.myurl,ivservison);

    }

    public void work(View view){
        switch (view.getId()){
            case R.id.bt_fankui:
                //启动反馈页面
                MainActivity.instance.agent.startFeedbackActivity();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
