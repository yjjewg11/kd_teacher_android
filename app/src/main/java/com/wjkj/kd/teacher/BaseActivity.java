package com.wjkj.kd.teacher;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.biz.Menu;

public class BaseActivity extends Activity implements View.OnClickListener
        //        ,GestureDetector.OnGestureListener
{
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.list.add(this);
    }



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
//
//    @Override
//    public boolean onDown(MotionEvent e) {
//        return false;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        return false;
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        return false;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        Log.i("TAG","打印移动事件"+e1.getX());
//        Log.i("TAG","再次打印移动事件an"+e2.getX());
//
//        return false;
//    }
}
