package com.wjkj.kd.teacher.com.wjkj.kd.teacher.biz;

import android.os.AsyncTask;
import android.util.Log;

import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.com.wjkj.kd.teacher.receiver.MyPushMessageReceiver;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class MyAsyncTask extends AsyncTask<Void,Void,Boolean>{

    boolean isnull = true;
    @Override
    protected Boolean doInBackground(Void... params) {
        long beginTime = System.currentTimeMillis();
        Log.i("TAG","工作线程已经开始执行");
        long currentTime = 0;
        while(isnull){
            try {
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentTime =System.currentTimeMillis()-beginTime;
            if(currentTime>120000) break;
            if(MainActivity.JESSIONID==null&& MyPushMessageReceiver.CHANNL_ID==null){
                continue;
            }else{
                isnull = false;
            }
        }

        if(currentTime>120000){return false;}
        return true;

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean){
            try {
                Log.i("TAG","不知道这个什么时候执行");
                MainActivity.instance.pushMessageToServer();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
