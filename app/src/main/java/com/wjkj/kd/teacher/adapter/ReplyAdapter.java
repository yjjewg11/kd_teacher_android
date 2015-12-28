package com.wjkj.kd.teacher.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.umeng.fb.model.Reply;
import com.wjkj.kd.teacher.R;
import com.wjkj.kd.teacher.utils.TimeUtils;
import com.wjkj.kd.teacher.views.CustomFankuiActivity;

public class ReplyAdapter extends BaseAdapter{
    LayoutInflater inflater;

    public ReplyAdapter(Context context){
        inflater  =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return CustomFankuiActivity.instance.getConvercations().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Reply reply = CustomFankuiActivity.instance.getConvercations().get(position);
        View view = null;
        if(reply.type.equals(Reply.TYPE_USER_REPLY)){
            view = View.inflate(CustomFankuiActivity.instance,R.layout.mineitem,null);
            TextView tv = (TextView)view.findViewById(R.id.imageViewUser);
            TextView tvTime = (TextView)view.findViewById(R.id.textViewtimeuser);
            tv.setText(reply.content);
            tvTime.setVisibility(View.GONE);
        }else{
            view = View.inflate(CustomFankuiActivity.instance,R.layout.serviceitem,null);
            TextView tv = (TextView)view.findViewById(R.id.imageViewservice);
            TextView tvTime = (TextView)view.findViewById(R.id.textViewTime);
            tv.setText(reply.content);
            tvTime.setText(""+ TimeUtils.getTime(reply.created_at));
        }

        return view;
//        Log.i("TAG","打印reply"+reply);
//        ViewHolder viewHolder = null;
//        if(convertView==null){
//            //客服
//
//            if(reply.type==Reply.TYPE_USER_REPLY){
//                convertView = inflater.inflate(R.layout.mineitem,null);
//                viewHolder = new ViewHolder();
//                viewHolder.imageView= (TextView) convertView.findViewById(R.id.imageViewUser);
//                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.textViewtimeuser);
//                viewHolder.state = "right";
//                convertView.setTag(viewHolder);
//
//            }else {
//                convertView = inflater.inflate(R.layout.serviceitem, null);
//                viewHolder = new ViewHolder();
//                viewHolder.imageView = (TextView) convertView.findViewById(R.id.imageViewservice);
//                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.textViewTime);
//                viewHolder.state = "left";
//                convertView.setTag(viewHolder);
//            }
//        }else{
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        Log.i("TAG","看看有没有执行到这里来");
//        Reply reply2 = CustomFankuiActivity.instance.getConvercations().get(position);
//        Log.i("TAG","打印类型"+reply2.type);
//        if(reply2.type.equals(Reply.TYPE_USER_REPLY)){
//            //用户信息
//            if(viewHolder.state.equals("left")) {
//                //重用布局如果为左，则重新生成布局.
//                convertView = View.inflate(CustomFankuiActivity.instance,R.layout.mineitem,null);
//                viewHolder = new ViewHolder();
//                viewHolder.imageView = (TextView) convertView.findViewById(R.id.imageViewUser);
//                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.textViewtimeuser);
//                convertView.setTag(viewHolder);
//                viewHolder.imageView.setVisibility(View.GONE);
//                viewHolder.imageView.setText(""+reply2.content);
//
//            }
//            viewHolder.imageView.setText(""+reply2.content);
//            viewHolder.tvTime.setVisibility(View.GONE);
//        }else{
//                //客服信息
//                Log.i("TAG","设置消息");
//            if(viewHolder.state.equals("right")){
//                convertView = View.inflate(CustomFankuiActivity.instance,R.layout.serviceitem,null);
//                viewHolder = new ViewHolder();
//                viewHolder.imageView = (TextView) convertView.findViewById(R.id.imageViewservice);
//                viewHolder.tvTime= (TextView) convertView.findViewById(R.id.textViewTime);
//                convertView.setTag(viewHolder);
////                viewHolder.tvTime.setVisibility(View.GONE);
//                viewHolder.imageView.setText("" + reply2.content);
//
//            }
//            viewHolder.imageView.setText(""+reply2.content);
//            viewHolder.tvTime.setText(""+ TimeUtils.getTime(reply2.created_at));
//        }
//        return convertView;
    }
}

class ViewHolder{
    TextView imageView;
    TextView tvTime;
    String state;
}

