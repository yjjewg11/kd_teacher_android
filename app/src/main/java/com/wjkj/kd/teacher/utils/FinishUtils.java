package com.wjkj.kd.teacher.utils;

import android.app.Activity;
import android.os.Handler;

import com.wjkj.kd.teacher.MyApplication;

import java.util.List;

public class FinishUtils {
    private static boolean f = true;

    //点击一次提醒，再点一次退出。
    /*
    * 此方法接受一个集合，可以帮你finish掉所有的activity
    * */
    public static <T> void finishProjectAgain(T t){
        if (f) {
            try {
                ToastUtils.showMessage("再按一次退出程序");
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            f = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    f = true;
                }
            }, 5000);
        } else {
            f = true;
            if (t instanceof List) {
                for (Activity activity : MyApplication.list) {
                    if (activity != null)
                        activity.finish();
                }
                System.exit(0);
            }
        }
    }
}
