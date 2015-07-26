package com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils;


import android.content.Context;
import android.widget.Toast;

import com.wjkj.kd.teacher.MainActivity;

public class ToastUtils {
    private static Context context;

    static{
        context = MainActivity.instance;
    }
    public static void showMessage(String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
