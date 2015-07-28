package com.wjkj.kd.teacher.com.wjkj.kd.teacher.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ParseUtils {

    //此方法得到一个bitmap对象，返回一个base64字符串
    public static String getBase64FromBitmap( Bitmap thePic){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        thePic.compress(Bitmap.CompressFormat.PNG,100,out);
        byte [] bytes = out.toByteArray();
        String pictureBytes = Base64.encodeToString(bytes, Base64.DEFAULT);
        Log.i("TAG", "打印一下字符串" + pictureBytes);
        try {
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pictureBytes;
    }
}
