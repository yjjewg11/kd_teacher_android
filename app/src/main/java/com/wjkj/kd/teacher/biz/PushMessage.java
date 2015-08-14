package com.wjkj.kd.teacher.biz;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.dao.AbstractDao;
import com.wjkj.kd.teacher.handle.AbstractHandle;
import com.wjkj.kd.teacher.receiver.MyPushMessageReceiver;
import com.wjkj.kd.teacher.utils.ExUtil;
import com.wjkj.kd.teacher.utils.GloableUtils;
import com.wjkj.kd.teacher.utils.HttpUtils;
import com.wjkj.kd.teacher.utils.ParseUtils;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class PushMessage extends AbstractDao{

    private AsyncHttpClient asyncHttpClient;
    public PushMessage(AsyncHttpClient asyncHttpClient){
        this.asyncHttpClient = asyncHttpClient;
    }
    //此方法调用网络请求传递参数
    public void pushMessageToServer() throws UnsupportedEncodingException, JSONException {

        //TODO
        MyPushMessageReceiver.f = false;
        String url = GloableUtils.InterfaceURL+ "pushMsgDevice/save.json";
//                +"?JSESSIONID="+JESSIONID;
        JSONObject jSONObject=new JSONObject();
        jSONObject.put("device_id", MainActivity.instance.device_token);
        jSONObject.put("device_type","android");
        jSONObject.put("status", GloableUtils.PUSH_STATE);
        StringEntity se = new StringEntity(jSONObject.toString(), HTTP.UTF_8);
        se.setContentEncoding(HTTP.UTF_8);
        RequestParams requestParams = new RequestParams();
        requestParams.put("JSESSIONID",GloableUtils.JESSIONID);
        String contentType = GloableUtils.APPLICATION_JSON + HTTP.CHARSET_PARAM +HTTP.UTF_8;
        Header[] headers=this.getHttpHeader();
//        headers = new BasicHeader[]{
//                new BasicHeader("Cookie","JSESSIONID="+GloableUtils.JESSIONID+";")
//        };

        asyncHttpClient.post(MainActivity.instance,url,headers,se,contentType,new AbstractHandle(){
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {

                    JSONObject jsonObject = ParseUtils.getJSONObject(bytes);
                    HttpUtils.pullJson(jsonObject);
                } catch (JSONException e) {
                    ExUtil.e(e);
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                super.onFailure(i,headers,bytes,throwable);
//                ToastUtils.showMessage("网络连接处上传失败");

            }
        });

    }

}
