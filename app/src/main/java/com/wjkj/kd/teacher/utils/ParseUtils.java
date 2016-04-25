package com.wjkj.kd.teacher.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ParseUtils {

    //此方法得到一个bitmap对象，返回一个base64字符串
    public static String getBase64FromBitmap( Bitmap thePic){
        String  pictureBytes = null;
        ByteArrayOutputStream out = null;
        byte[] bytes = null;
        try {
            int quality = 80;
        out = new ByteArrayOutputStream();
            do {
                thePic.compress(Bitmap.CompressFormat.JPEG, quality, out);
                bytes = out.toByteArray();
                quality-=10;
                if(quality==0) break;
                out.reset();
            }while (bytes.length / 1024 > 100);
            try {
                if(out!=null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            builder = bytes.toString();
//                    Base64.encodeToString(bytes, Base64.DEFAULT);
//            if(thePic!=null){
//                thePic.recycle();
//                System.gc();
//            }

        pictureBytes = Base64.encodeToString(bytes,Base64.NO_WRAP);


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return pictureBytes;
    }

    public static JSONObject getJSONObject(byte[] bytes) throws JSONException {
        String shuzu = new String(bytes);

        return new JSONObject(shuzu);
    }
}
