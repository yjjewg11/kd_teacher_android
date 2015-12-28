package com.wjkj.kd.teacher.views;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.wjkj.kd.teacher.BaseActivity;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.R;
import com.wjkj.kd.teacher.biz.DealWithPushMessage;
import com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.utils.GloableUtils;
import com.wjkj.kd.teacher.utils.ToastUtils;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class PushStateActivity extends BaseActivity {

    private CheckSwitchButton checkswhbt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_state);
        checkswhbt = (CheckSwitchButton)findViewById(R.id.bt_switchbutton);
        int checked = MainActivity.instance.sp.getInt("isChecked",0);
        if(checked==0){
            checkswhbt.setChecked(true);
        }else{
            checkswhbt.setChecked(false);
        }
//        if(sp.getString("isSave","null").equals("null")){
//            checkswhbt.setChecked(true);
//        }


        checkswhbt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changPushState(isChecked);
            }
        });

    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_tuisong_settings:
                finish();
                break;
        }
    }


    private void changPushState(boolean ischecked) {
        try {
            if(ischecked) {
                GloableUtils.PUSH_STATE=0;
                MainActivity.instance.editor.putInt("isChecked",0);
            }else{
                GloableUtils.PUSH_STATE=2;
                MainActivity.instance.editor.putInt("isChecked",2);
            }
            MainActivity.instance.editor.commit();
            DealWithPushMessage.dealPushMessage();
            ToastUtils.showMessage(GloableUtils.PUSH_STATE==0? "消息通知已成功开启!":"消息通知已成功关闭");
        }catch (NullPointerException e){
            ExUtil.e(e);
        } catch (JSONException e) {
            ExUtil.e(e);
        } catch (UnsupportedEncodingException e) {
            ExUtil.e(e);
        }
    }
    //判断状态是否改变，如果该变再判断是什么状态


}
