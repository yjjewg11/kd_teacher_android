package com.wjkj.kd.teacher.com.wjkj.kd.teacher.entity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

import com.wjkj.kd.teacher.MyApplication;
import com.wjkj.kd.teacher.SettingActivity;

public class MyWebView extends WebView{
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }



    float x,y;
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //按下之后记录坐标
                x = event.getX();
                y = event.getY();
                Log.i("TAG", "打印按下事件");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("TAG","打印移动事件");
                break;
            case MotionEvent.ACTION_UP:
                //抬起时触发事件
                Log.i("TAG","打印抬起事件"+"kuan ==   "+event.getX());
                if(x>400&&(x-event.getX()>130)){
                    //启动设置界面
                    MyApplication.instance.startActivity(new Intent(MyApplication.instance,SettingActivity.class));
                }
                break;
        }
        return onTouchEvent(event);

    }
}
