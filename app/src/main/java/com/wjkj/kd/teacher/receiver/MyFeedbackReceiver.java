package com.wjkj.kd.teacher.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.baidu.android.feedback.BDFeedbackReceiver;
import com.baidu.android.feedback.ui.FeedbackActivity;
import com.wjkj.kd.teacher.R;

public class MyFeedbackReceiver extends BDFeedbackReceiver {
    @Override
    public void onFBMessage(Context context, String fbMsg) {

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String hint = "通知";
// context.getString(R.string.text_nofity_hint);
        Notification n = new Notification(R.drawable.ic_launcher,
                hint, System.currentTimeMillis());
        n.flags = Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage(context.getPackageName());
        // PendingIntent
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                R.string.bd_fb_feedback, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        n.setLatestEventInfo(context, hint, fbMsg, contentIntent);
        nm.notify(R.string.bd_fb_feedback, n);
    }
}
