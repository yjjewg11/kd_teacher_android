package me.nereo.imagechoose.utils;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * TimeUtil 时间工具类
 *
 * @author weiwu.song
 * @data: 2015/6/9 10:19
 * @version: v1.0
 */
public final class TimeUtil {
    private final static int DELETE_DAY = 15;
    //时间格式
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy-MM-dd");

    public static SimpleDateFormat formatString = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    private TimeUtil() {
    }

    public static String getYMDHMSFromMillion(long million){
        return format.format(new Date(million));
    }

    public static Long getMillionFromYMD(String time) {
        try {
            return formatYMD.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date formatDate(String time) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static long getYMDHMSTime(String time){
        try {
            return format.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getYMDHMSFromY_M_D_M_H_S(String date) {
        Date dateYMDHMS = getDateFromString(date);
        return format.format(dateYMDHMS);
    }

    public static String getStringDate(Date date) {
        return formatString.format(date);
    }

    public static Date getDateFromString(String date) {
        try {
            return formatString.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getYMDTimeFromYMDHMS(String time) {
        Date date = null;
        try {
            if (TextUtils.isEmpty(time)) {
                date = new Date();
            } else {
                date = format.parse(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getYMDTimeFromDate(date);
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDate() {
        Date date = new Date();
        return format.format(date);
    }
    public static long getNowTime(){
        Date date = new Date();
        return date.getTime();
    }
    public static long getYMDnowTime(){
        try {
            return format.parse(getNowDate()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getWeekOfDay(String date) {
        String mWay = getDay(date);
        return "星期" + mWay;
    }


    //通过yyyy-MM-dd格式的时间得到星期几
    @NonNull
    public static String getDay(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            c.setTime(format.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mWay = c.get(Calendar.DAY_OF_WEEK) + "";
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return mWay;
    }


    public static int getWeekOfDayNum(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            c.setTime(format.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static String getYMDTimeFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    //通过yyyy-MM-dd格式的时间得到毫秒数

    public static Long getMullions(String date) {
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {


        }
        return d.getTime();
    }


    //获取的毫秒数转化为时间
    public static String getDateToString(Long m) {
        Date c = new Date();
        c.setTime(m);
        return formatYMD.format(c);
    }

    public static long compareTime(Date date, String plandate) {
        String th = formatYMD.format(date);
        Date date2 = null;
        Date date3 = null;
        try {
            date2 = formatYMD.parse(th);
            date3 = formatYMD.parse(plandate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date3.getTime() - date2.getTime();
    }

//    public static <T>void sortList(List<T> )
}
