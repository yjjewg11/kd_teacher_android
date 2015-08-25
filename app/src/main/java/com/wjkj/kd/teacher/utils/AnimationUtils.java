package com.wjkj.kd.teacher.utils;


import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtils {

    public static  void startMyAnimation(long fromx,long tox,long fromy,long toy,View t){
        Log.i("TAG", "难道动画又开启了");
        againStartMyAnimation(fromx,tox,fromy,toy,t,true);
    }

    //true需要，false不需要
    public static void againStartMyAnimation(long fromx,long tox,long fromy,long toy,View t,boolean f){
        if(f) {
            Animation animation = new TranslateAnimation(fromx, tox, fromy, toy);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            t.startAnimation(animation);
        }
    }

    public static void hideRadio(View view){
        hideRadioSlowy(view,0);
    }

    public static void hideRadioSlowy(View view, long mi){
        Animation animation = new TranslateAnimation(0,0,0,100);
        animation.setDuration(mi);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void hideView(View view){
        hideRadioSlowy(view,0);
    }

}
