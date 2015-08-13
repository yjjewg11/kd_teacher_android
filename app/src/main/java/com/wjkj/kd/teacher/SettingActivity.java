package com.wjkj.kd.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.umeng.update.UmengUpdateAgent;
import com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.utils.GloableUtils;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class SettingActivity extends BaseActivity {

    private RelativeLayout rlPush,rlAboutUs,rlFankui,rlUpdate,rlCancle,rlFinish;
    private RelativeLayout[] relativeLayouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setViews();
        setListeners();
    }

    //给图片按钮添加监听事件
    private void setListeners() {

        for(RelativeLayout relativeLayout :relativeLayouts){
            relativeLayout.setOnClickListener(this);
        }


    }

    private void setViews() {
        rlPush = (RelativeLayout)findViewById(R.id.r2_push);
        rlAboutUs = (RelativeLayout)findViewById(R.id.r3_aboutus);
        rlFankui = (RelativeLayout)findViewById(R.id.r4_fankui);
        rlUpdate  = (RelativeLayout)findViewById(R.id.r5_update);
        rlCancle = (RelativeLayout)findViewById(R.id.r6_cacle);
        rlFinish = (RelativeLayout)findViewById(R.id.rl_finish);
        relativeLayouts =
                new RelativeLayout[]{rlPush,rlAboutUs,rlFankui,rlUpdate,rlCancle,rlFinish};

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //推送消息/表示默认开启状态
            case R.id.r2_push:changPushState();break;
            //回退
            case R.id.r3_aboutus: startActivity(new Intent(this, AboutUsActivity.class));  break;
            //关于我们
            case R.id.r4_fankui: MainActivity.instance.agent.startFeedbackActivity();   break;
            //意见反馈
            case R.id.r6_cacle: MainActivity.instance.webView.loadUrl("javascript:G_jsCallBack.userinfo_logout()");  finish(); break;
            //注销用户
//            case R.id.r6_cacle:
//                MainActivity.instance.webView.loadUrl(GloableUtils.CANCLE_USER);
//                finish();
//                break;
            //手动更新
            case R.id.r5_update: UmengUpdateAgent.forceUpdate(MainActivity.instance); break;
            //结束此页面
            case R.id.rl_finish: finish(); break;
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
