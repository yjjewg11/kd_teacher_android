package com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtils {

    public static Bitmap compressPictureFromFile(String pathName){
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = true;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName,options);
    }
}
