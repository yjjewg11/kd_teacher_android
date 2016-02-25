package com.wjkj.kd.teacher.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.umeng.fb.SyncListener;
import com.umeng.fb.fragment.FeedbackFragment;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;
import com.wjkj.kd.teacher.BaseActivity;
import com.wjkj.kd.teacher.MainActivity;
import com.wjkj.kd.teacher.R;
import com.wjkj.kd.teacher.adapter.ReplyAdapter;
import com.wjkj.kd.teacher.utils.StringUtils;
import com.wjkj.kd.teacher.utils.ToastUtils;

import java.util.List;

public class CustomFankuiActivity extends BaseActivity {
    private static Conversation conversation;
    private ListView listView;
    private EditText editfankui;
    private ReplyAdapter replyAdapter;
    public static CustomFankuiActivity instance;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_fankui);
        instance = this;
        setViews();
//        replyAdapter = new ReplyAdapter(this);
//        listView.setAdapter(replyAdapter);

    }
    private void setViews() {
//        listView = (ListView)findViewById(R.id.listView);
        editfankui = (EditText)findViewById(R.id.edit_fan_kui);
        image = (ImageView)findViewById(R.id.iv_custom_back);
        image.setOnClickListener(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        new FeedbackFragment().refresh();
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttononCLick:
                //反馈提交
                pushFanKui();
                break;
            case R.id.iv_custom_back:
                finish();
                break;
//            case R.id.buttonsend:
                //点击发送添加消息
//                String text = editText.getText().toString();
//                if(StringUtils.checkIsNull(text)){
//                    editText.setError("不能发送空消息");
//                    return;
//                }
//
//
////                editText.setText("");
//                conversation.addUserReply(""+text);
//                conversation.sync(new SyncListener() {
//                    @Override
//                    public void onReceiveDevReply(List<Reply> list) {
//
//                    }
//
//                    @Override
//                    public void onSendUserReply(List<Reply> list) {
//
//                    }
//                });
//                replyAdapter.notifyDataSetChanged();
//                break;
        }
    }

    private void pushFanKui() {
        String text = editfankui.getText().toString();
        if(StringUtils.checkIsNull(text)){
            ToastUtils.showMessage("消息不能为空哦！");
            return;
        }

        Conversation conversation = MainActivity.instance.agent.getDefaultConversation();
        conversation.addUserReply(MainActivity.instance.getLoginName()+""+text);
        conversation.sync(new SyncListener() {
            @Override
            public void onReceiveDevReply(List<Reply> list) {

            }

            @Override
            public void onSendUserReply(List<Reply> list) {
                ToastUtils.showMessage("反馈已成功提交");
            }
        });
        finish();
    }

    public List<Reply> getConvercations(){
        conversation  =  MainActivity.instance.agent.getDefaultConversation();
        return conversation.getReplyList();
    }
}
