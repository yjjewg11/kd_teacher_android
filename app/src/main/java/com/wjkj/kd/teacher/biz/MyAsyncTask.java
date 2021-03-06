package com.wjkj.kd.teacher.biz;

import android.os.AsyncTask;

import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.utils.GloableUtils;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class MyAsyncTask extends AsyncTask<Void,Void,Boolean>{

    boolean isnull = true;
    @Override
    protected Boolean doInBackground(Void... params) {
        long beginTime = System.currentTimeMillis();
        long currentTime = 0;
        while(isnull){
            try {
                Thread.sleep(5000);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                ExUtil.e(e);
            }
            currentTime =System.currentTimeMillis()-beginTime;
            if(currentTime>120000) break;
            if(MainActivity.instance.JESSIONID==null&& GloableUtils.CHANNL_ID==null){
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
                if(MainActivity.instance==null){return;}
                MainActivity.instance.pushMessage.pushMessageToServer();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            MainActivity.instance.myAsyncTask.cancel(true);
        }
    }
}
