package com.wjkj.kd.teacher.utils;
import android.widget.Toast;
import com.wjkj.kd.teacher.MainActivity;
public class ToastUtils {
    public static void showMessage(final String message){
        Toast.makeText(MainActivity.instance, message, Toast.LENGTH_LONG).show();
    }
}
