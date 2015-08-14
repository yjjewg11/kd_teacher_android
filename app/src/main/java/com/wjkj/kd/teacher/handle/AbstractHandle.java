package com.wjkj.kd.teacher.handle;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wjkj.kd.teacher.utils.ExUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractHandle extends AsyncHttpResponseHandler {
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        try {

            String shuzu = new String(responseBody);
            JSONObject jsonObject = new JSONObject(shuzu);

        } catch (JSONException e) {
            ExUtil.e(e);
        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        try {
//            ToastUtils.showMessage("statusCode=" + statusCode);
//            String shuzu = new String(responseBody);
//            JSONObject jsonObject = new JSONObject(shuzu);
//            HttpUtils.pullJson(jsonObject);
            Log.i("TAG","打印错误日志");
        } catch (Exception e) {
            ExUtil.e(e);
        }
    }
}
