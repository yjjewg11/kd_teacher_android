package com.wjkj.kd.teacher.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static String getTime(Long time){
     return  new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));

    }
}
