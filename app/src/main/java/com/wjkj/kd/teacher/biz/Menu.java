//package com.wjkj.kd.teacher.biz;
//
//import android.app.Activity;
//import android.util.Log;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.wjkj.kd.teacher.MainActivity;
//import com.wjkj.kd.teacher.R;
//
//public class Menu {
//
//    Activity activity;
//    SlidingMenu slidingMenu;
//
//    public Menu(Activity activity) {
//        super();
//        RelativeLayout[] relativeLayouts = new RelativeLayout[3];
//        this.activity = activity;
//        slidingMenu = new SlidingMenu(activity);
//        slidingMenu.setMenu(R.layout.leftmenu);
//        slidingMenu.attachToActivity(activity, SlidingMenu.SLIDING_CONTENT);
//        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
//        slidingMenu.setBehindOffset(screenWidth * 3 / 8);
//        View rlInflate = slidingMenu.getMenu();
// //        收藏按钮
//        relativeLayouts[0] = (RelativeLayout)rlInflate.findViewById(R.id.rl_gaiming);
//        relativeLayouts[1] = (RelativeLayout)rlInflate.findViewById(R.id.rl_gaimi);
//        relativeLayouts[2] = (RelativeLayout)rlInflate.findViewById(R.id.rl_shoucang);
//        RelativeLayout rl2 = (RelativeLayout) rlInflate.findViewById(R.id.rl_gaiming);
//        Log.i("TAG","查看监听事件是否已经添加");
//        MyListener myListener = new MyListener();
//        for (RelativeLayout rl : relativeLayouts) {
//            rl.setOnClickListener(myListener);
//            Log.i("TAG","看看是否已经添加监听");
//        }
//    }
//    //控件点击事件无任何作用：
//       //1.
//
//
//
//    public void showMenu() {
//        slidingMenu.showMenu();
//    }
//    class MyListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//            //如果R.id.*报错
//
//            Log.i("TAG","监听事件已经添加完毕，看看点击是否响应");
//            //用if
////			if (v.getId()==R.id.btn_leftmenu_allTopic)
////			{index=0;}
//            try {
//                switch (v.getId()) {
//
//                    case R.id.rl_shoucang:
//
//                        break;
//                    case R.id.rl_gaiming:
//                        //调用js接口修改联系人信息
//                        Log.i("TAG","修改信息方法已经执行");
//                        MainActivity.instance.webView.loadUrl("javascript:G_jsCallBack.user_info_update()");
//                        break;
//                    case R.id.rl_gaimi:
//                        //调用js接口修联系人密码
//                        Log.i("TAG","修改密码方法已经执行");
//                        MainActivity.instance.webView.loadUrl("javascript:G_jsCallBack.user_info_updatepassword()");
//                        break;
//
//                }
//            }finally {
//                slidingMenu.toggle();
//            }
//
//        }
//
//
//
//    }
//
//
//
//
//
//
////    public void  pushMessage(){
////        try {
////            MainActivity.instance.pushMessage.pushMessageToServer();
////        } catch (UnsupportedEncodingException e) {
////            e.printStackTrace();
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////    }
//}
