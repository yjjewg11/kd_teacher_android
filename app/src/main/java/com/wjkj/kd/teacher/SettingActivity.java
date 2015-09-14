package com.wjkj.kd.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.wjkj.kd.teacher.utils.ToastUtils;
import com.wjkj.kd.teacher.utils.Util;
import com.wjkj.kd.teacher.views.CustomFankuiActivity;
import com.wjkj.kd.teacher.views.PushStateActivity;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class SettingActivity extends BaseActivity {

    private RelativeLayout rlPush,rlAboutUs,rlFankui,rlUpdate,rlCancle,rlAlertName,rlAlertPwd;
    private RelativeLayout[] relativeLayouts;
    private RelativeLayout rlSettingFinish;
    private RelativeLayout rl_clean_cache;
    private TextView tv_cache;

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
        rlAlertName = (RelativeLayout)findViewById(R.id.r2_gaiming);
        rlAlertPwd = (RelativeLayout)findViewById(R.id.r2_gaimi);
        rlSettingFinish = (RelativeLayout)findViewById(R.id.rl_seting_finish);
        rl_clean_cache = (RelativeLayout)findViewById(R.id.r2_clean_cache);
        relativeLayouts =
                new RelativeLayout[]{rlPush,rlAboutUs,rlFankui,rlUpdate,rlCancle
                 ,rlAlertName,rlAlertPwd,rlSettingFinish,rl_clean_cache};
        tv_cache = (TextView)findViewById(R.id.tv_cache);
        showCache();

    }

    private double allsize = 0;
    //动态显示缓存大小
    private void showCache() {
        Util.setDoMyThing(new Util.DoMything() {
            @Override
            public boolean doOwnThing(File file) {
                //遍历获得所有目录的大小
                allsize += file.length();
                return true;
            }
        });
        Util.deleteDir(new File(getCacheDir().toString()));
        String size = String.valueOf((allsize / 1024 / 1024 ));
        tv_cache.setText("" + size.substring(0,(size.indexOf(".")+2))+" M");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.r2_clean_cache:
                //清楚缓存
                MainActivity.instance.clear();
                ToastUtils.showMessage("缓存清除成功!");
                MainActivity.instance.webView.loadUrl(MainActivity.instance.webUrl);
                finish();
                break;

            case R.id.rl_seting_finish:
                //结束页面
                finish();
                break;
            //推送消息/表示默认开启状态
            case R.id.r2_push:
//                changPushState();
                MainActivity.instance.startActivity(new Intent(MainActivity.instance, PushStateActivity.class));
//                MainActivity.instance.agent.startFeedbackActivity();
                break;
            //回退
            case R.id.r3_aboutus: startActivity(new Intent(this, AboutUsActivity.class));  break;
            //关于我们
            case R.id.r4_fankui:
//                MainActivity.instance.agent.startFeedbackActivity();
//                MainActivity.instance.agent.startFeedbackActivity2();
                MainActivity.instance.startActivity(new Intent(this, CustomFankuiActivity.class));
                break;
            //意见反馈

            case R.id.r6_cacle:
                MainActivity.instance.webView.loadUrl("javascript:G_jsCallBack.userinfo_logout()");
                try {
                    MainActivity.instance.hideBottomAfaterCancle();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                finish();
                break;
            //注销用户
//            case R.id.r6_cacle:
//                MainActivity.instance.webView.loadUrl(GloableUtils.CANCLE_USER);
//                finish();
//                break;
            //手动更新
            case R.id.r5_update: UmengUpdateAgent.forceUpdate(MainActivity.instance);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

                    @Override
                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                        switch (i) {
                            case UpdateStatus.No:
                                ToastUtils.showMessage("当前已是最新版本！");
                                break;
                        }
                    }
                });
                break;
            //结束此页面
            case R.id.r2_gaiming: MainActivity.instance.webView.loadUrl("javascript:G_jsCallBack.user_info_update()");  finish(); break;
            case R.id.r2_gaimi: MainActivity.instance.webView.loadUrl("javascript:G_jsCallBack.user_info_updatepassword()");  finish(); break;
        }
    }


}
