package com.wjkj.kd.teacher.biz;

import android.content.Context;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.push.FeedbackPush;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.handle.MyUmengMessageHandle;
import com.wjkj.kd.teacher.views.CustomFankuiActivity;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

/**@
 * 此类用于启动友盟的服务。
 *
*/
public class RegistUmengService {
    private Context context;
    public UMSocialService mController;
    public static final String appId = "wxc784adf432c9f59d";
    public static final String secret = "078b3b3e3515f1d434c87d20dc02ab8c";
    public RegistUmengService(Context context){
        this.context = context;
    }
    public void registService() throws UnsupportedEncodingException, JSONException {
        initfankui();
        //友盟自动更新服务搬迁到myapplication中去，目的让应用程序已启动就提示自动更新，此方法已废弃，程序易崩溃
        initPushMessage();
        initUpdateApk();
    }

    //初始化友盟更新
    private void initUpdateApk() {
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setDefault();
        UmengUpdateAgent.setDeltaUpdate(false);

        //设置自动更新监听
        UmengUpdateAgent.update(MainActivity.instance);
        UpdateConfig.setDebug(true);
    }

//    //初始化友盟分享
//    private void initSocialSDK() {
//        //qq分享
//
//        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(MainActivity.instance,"100424468",
//                "SumAAk7jtaUSnZqd");
//        qqSsoHandler.addToSocialSDK();
//        //微信分享
//        UMWXHandler wxHandler = new UMWXHandler(MainActivity.instance,appId,secret);
//        wxHandler.addToSocialSDK();
//
//        //朋友圈
//        UMWXHandler wxCircleHandler = new UMWXHandler(MainActivity.instance,appId,secret);
//        wxCircleHandler.setToCircle(true);
//        wxCircleHandler.addToSocialSDK();
//
//        //qq空间分享
//        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(MainActivity.instance, "100424468","SumAAk7jtaUSnZqd");
//        qZoneSsoHandler.addToSocialSDK();
//        com.umeng.socialize.utils.Log.LOG = true;
//        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//        mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
//        mController.getConfig().removePlatform(SHARE_MEDIA.QQ);
//        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
//    }
//
//    public void setShareContent(String content,String picpath,String links){
//
////        if(TextUtils.isEmpty(links)){
////
////            // 设置分享内容
//            mController.setShareContent(content);
//
//// 设置分享图片, 参数2为图片的url地址
//            mController.setShareMedia(new UMImage(MainActivity.instance, picpath));
////            return;
////        }
//
//        mController.setAppWebSite(SHARE_MEDIA.WEIXIN,"http://www.baidu.com");
//        mController.setAppWebSite(SHARE_MEDIA.WEIXIN_CIRCLE,"http://www.baidu.com");
//
//
//        WeiXinShareContent weixinContent = new WeiXinShareContent();
//////设置title
//        weixinContent.setTitle("友盟社会化分享组件-微信");
////设置分享内容跳转URL
//        weixinContent.setTargetUrl("http://www.baidu.com");
//        weixinContent.setShareContent("友盟社会化分享组件-微信");
////设置分享图片
//        weixinContent.setShareImage(new UMImage(MainActivity.instance,"/storage/emulated/0/DCIM/Camera/1437462568076.jpg"));
//        mController.setShareMedia(weixinContent);
//
////设置微信朋友圈分享内容
//        CircleShareContent circleMedia = new CircleShareContent();
//        circleMedia.setShareImage(new UMImage(MainActivity.instance,"/storage/emulated/0/DCIM/Camera/1437462568076.jpg"));
//        circleMedia.setTargetUrl("http://www.baidu.com");
//        circleMedia.setShareContent("友盟社会化分享组件-微信");
//        circleMedia.setTitle("友盟社会化分享组件-微信");
//        mController.setShareMedia(circleMedia);
//
//    }

    //友盟推送
    private void initPushMessage() throws UnsupportedEncodingException, JSONException {
        PushAgent mPushAgent = PushAgent.getInstance(context);
        ownNotification(mPushAgent);
        mPushAgent.enable();
        MainActivity.instance.device_token = UmengRegistrar.getRegistrationId(context);
        ((MainActivity)context).isFirstSendMessage();

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
