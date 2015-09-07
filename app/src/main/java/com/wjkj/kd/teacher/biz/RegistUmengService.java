package com.wjkj.kd.teacher.biz;

import android.content.Context;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.handle.MyUmengMessageHandle;
import com.wjkj.kd.teacher.views.CustomFankuiActivity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**@
 * 此类用于启动友盟的四个服务。
 *
*/
public class RegistUmengService {
    private Context context;
    public UMSocialService mController;

    public RegistUmengService(Context context){
        this.context = context;
    }
    public void registService() throws UnsupportedEncodingException, JSONException {
        initfankui();
        initUpdateApk();
        initPushMessage();
        initSocialSDK();
    }

    //初始化友盟分享
    private void initSocialSDK() {
        //qq分享

        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MainActivity.instance,"100424468",
                "SumAAk7jtaUSnZqd");
        qqSsoHandler.addToSocialSDK();


        //微信分享
        UMWXHandler wxHandler = new UMWXHandler(MainActivity.instance,"2868383197","60248f289483fef5b6b3e15f26c7491a");
        wxHandler.addToSocialSDK();

        //朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(MainActivity.instance,"2868383197","60248f289483fef5b6b3e15f26c7491a");
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

        //qq空间分享
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(MainActivity.instance, "100424468","SumAAk7jtaUSnZqd");
        qZoneSsoHandler.addToSocialSDK();
        com.umeng.socialize.utils.Log.LOG = true;
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
        mController.getConfig().removePlatform(SHARE_MEDIA.QQ);
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);

    }

    public void setShareContent(String content,String pathUrl) {
 // 设置分享内容
        mController.setShareContent(content);
// 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(MainActivity.instance, pathUrl));
    }

    //初始化友盟更新
    private void initUpdateApk() {
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setDefault();
        //设置自动更新监听
        UmengUpdateAgent.update(MainActivity.instance);
        UpdateConfig.setDebug(true);
    }

    //友盟推送
    private void initPushMessage() throws UnsupportedEncodingException, JSONException {
        PushAgent mPushAgent = PushAgent.getInstance(context);
        ownNotification(mPushAgent);
        mPushAgent.enable();
        MainActivity.instance.device_token = UmengRegistrar.getRegistrationId(context);
        DealWithPushMessage.dealPushMessage();
    }

    //当通知来临时，点亮图标
    private void ownNotification(PushAgent mPushAgent) {

        //通知来临时，加上自己的业务处理逻辑
        UmengMessageHandler umengMessageHandler = new MyUmengMessageHandle();

        mPushAgent.setMessageHandler(umengMessageHandler);
    }

    //友盟反馈
    private void initfankui() {
        MainActivity.instance.agent = new FeedbackAgent(context);
        MainActivity.instance.agent.setWelcomeInfo("欢饮来到反馈中心，您宝贵的意见是对我们最大的支持");
        FeedbackPush.getInstance(context).init(CustomFankuiActivity.class, true);
        MainActivity.instance.agent.sync();
        MainActivity.instance.agent.openFeedbackPush();
        FeedbackPush.getInstance(context).init(true);
    }
}
