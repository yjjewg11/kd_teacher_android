package com.wjkj.kd.teacher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;
import com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.utils.GloableUtils;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class SettingActivity extends BaseActivity {

    private ImageView imageBack,imagePush,imageAbout,imageSugesstion;
    private TextView textFinish,tvUpdate;
    private ImageView imageUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setViews();
        setListeners();
    }

    //给图片按钮添加监听事件
    private void setListeners() {
        ImageView[] imageViews = new ImageView[]{imageAbout,imageBack,imagePush,imageSugesstion,imageUpdate};
        for(ImageView imageView :imageViews){
            imageView.setOnClickListener(this);
        }
        textFinish.setOnClickListener(this);

    }

    private void setViews() {
        imageBack = (ImageView)findViewById(R.id.imageView8);
        imagePush = (ImageView)findViewById(R.id.imageView2);
        imageAbout = (ImageView)findViewById(R.id.imageView9);
        imageSugesstion = (ImageView)findViewById(R.id.imageView10);
        imageUpdate = (ImageView)findViewById(R.id.imageView4);
        textFinish = (TextView)findViewById(R.id.textView21);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //推送消息/表示默认开启状态
            case R.id.imageView2:changPushState();break;
            //回退
            case R.id.imageView8: finish();  break;
            //关于我们
            case R.id.imageView9: startActivity(new Intent(this,AboutUsActivity.class)); break;
            //意见反馈
            case R.id.imageView10: MainActivity.instance.agent.startFeedbackActivity();  break;
            //结束程序
            case R.id.textView21:
                for(Activity activity:MyApplication.list){
                    activity.finish();
                    System.exit(0);
                }
                break;
            //手动更新
            case R.id.imageView4: UmengUpdateAgent.forceUpdate(MainActivity.instance); break;
        }
    }

    private void changPushState() {
        try {
            if (GloableUtils.PUSH_STATE == 0) {
                GloableUtils.PUSH_STATE = 1;
                MainActivity.instance.pushMessage.pushMessageToServer();
            }else{
                GloableUtils.PUSH_STATE=0;
                MainActivity.instance.pushMessage.pushMessageToServer();
            }
        }catch (NullPointerException e){
            ExUtil.e(e);
        } catch (JSONException e) {
           ExUtil.e(e);
        } catch (UnsupportedEncodingException e) {
           ExUtil.e(e);
        }
    }
}
