package com.wjkj.kd.teacher.utils;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ParseUtils {

    //此方法得到一个bitmap对象，返回一个base64字符串
    public static String getBase64FromBitmap( Bitmap thePic){
        Log.i("TAG","如果这个方法都执行了，就不是数组的原因");
        String  pictureBytes = null;
        ByteArrayOutputStream out = null;
        byte[] bytes = null;
        try {
            int quality = 80;
            int picSize = 200;
        out = new ByteArrayOutputStream();
            Log.i("TAG","打印最大内存"+Runtime.getRuntime().maxMemory()/1024/1024 +"  Mb");
            Log.i("TAG","打印空闲内存"+Runtime.getRuntime().freeMemory()/1024/1024 +"  Mb");
            Log.i("TAG", "打印总内存" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "  Mb");
            if(Runtime.getRuntime().freeMemory()/1024/1024<=5){
                picSize = 100;
            }
            do {
                thePic.compress(Bitmap.CompressFormat.JPEG, quality, out);
                bytes = out.toByteArray();
                quality-=10;
                if(quality==0) break;
                out.reset();
            }while (bytes.length>picSize*1024);
            Log.i("TAG", "打印一dfsdfsfsdf下字符串" + bytes.length);
            Log.i("TAG","打印字节大小：==="+bytes.length/1024+ "   kb");
            try {
                if(out!=null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            builder = bytes.toString();
//                    Base64.encodeToString(bytes, Base64.DEFAULT);
            if(thePic!=null){
                thePic.recycle();
                System.gc();
            }

        pictureBytes = Base64.encodeToString(bytes,Base64.NO_WRAP);


        }catch (Exception e){
            ExUtil.e(e);
        }
        return pictureBytes;
    }

    public static JSONObject getJSONObject(byte[] bytes) throws JSONException {
        String shuzu = new String(bytes);


        return new JSONObject(shuzu);
    }
}
