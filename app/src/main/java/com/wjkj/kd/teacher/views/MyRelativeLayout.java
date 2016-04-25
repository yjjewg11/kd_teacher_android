package com.wjkj.kd.teacher.views;
/*
* 此类继承main布局，监听输入法弹出布局重绘，大小改变
* */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.wjkj.kd.teacher.MainActivity;

public class MyRelativeLayout extends RelativeLayout{
    public MyRelativeLayout(Context context) {
        super(context);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("TAG","打印布局高度是否有变化"+"w  ==="+w+"   h==="+h+"   oldw=="+oldw+"   oldh=="+oldh);
        //输入法弹起时，现有布局高度h变小,隐藏底部菜单
        if(h<oldh){
            if(!TextUtils.isEmpty(MainActivity.instance.JESSIONID)){
                MainActivity.instance.radioGroup.setVisibility(View.GONE);
                MainActivity.instance.tv_line.setVisibility(View.GONE);

            }else{
                MainActivity.instance.radioGroup.setVisibility(View.VISIBLE);
                MainActivity.instance.tv_line.setVisibility(View.VISIBLE);
            }
        }

    }
}
