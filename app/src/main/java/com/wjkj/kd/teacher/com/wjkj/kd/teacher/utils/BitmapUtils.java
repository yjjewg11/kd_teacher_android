package com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {

    public static Bitmap compressPictureFromFile(String pathName){
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(pathName, options);
            options.inJustDecodeBounds = true;
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            options.inSampleSize = 3;
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(pathName, options);
        }finally {
            if(bitmap!=null)
            recyleBitmap(bitmap);
        }
    }


    //此方法用于释放bitmap

    public static  void recyleBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
            bitmap.recycle();
            System.gc();
        }
    }
}
