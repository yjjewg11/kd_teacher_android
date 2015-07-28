package com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils;


import android.widget.Toast;

import com.wjkj.kd.teacher.MainActivity;

public class ToastUtils {
    public static void showMessage(String message){
        Toast.makeText(MainActivity.instance,message,Toast.LENGTH_LONG).show();
    }
}
