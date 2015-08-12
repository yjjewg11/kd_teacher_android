package com.wjkj.kd.teacher.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ParseUtils {

    //此方法得到一个bitmap对象，返回一个base64字符串
    public static String getBase64FromBitmap( Bitmap thePic){
        Log.i("TAG","如果这个方法都执行了，就不是数组的原因");
        StringBuilder builder = null;
        String  pictureBytes = null;
        ByteArrayOutputStream out = null;
        try {
        out = new ByteArrayOutputStream();
        thePic.compress(Bitmap.CompressFormat.PNG,100,out);
        byte[] bytes = out.toByteArray();
            Log.i("TAG","打印一dfsdfsfsdf下字符串"+bytes.toString());

//            builder = bytes.toString();
//                    Base64.encodeToString(bytes, Base64.DEFAULT);

        pictureBytes = Base64.encodeToString(bytes,Base64.DEFAULT);


        }catch (Exception e){
            ExUtil.e(e);
        }finally {
            try {
                if(out!=null)
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pictureBytes;
    }
}
