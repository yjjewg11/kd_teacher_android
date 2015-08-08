package com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils;


import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtils {

    public static  void startMyAnimation(long fromx,long tox,long fromy,long toy,View view){
        Animation animation = new TranslateAnimation(fromx,tox,fromy,toy);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void hideRadio(View view){
        Animation animation = new TranslateAnimation(0,0,0,100);
        animation.setDuration(0);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

}
