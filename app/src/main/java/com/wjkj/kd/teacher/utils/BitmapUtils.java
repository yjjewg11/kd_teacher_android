package com.wjkj.kd.teacher.utils;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.wjkj.kd.teacher.MainActivity;

import java.io.File;

public class BitmapUtils {


    //根据图片的路径名获取bitmap对象并将图片上传
    public static Bitmap compressPictureFromFile(String pathName) {


        BitmapFactory.Options options = null;
        try {
            options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, options);
            int scale = 1;
            while (options.outWidth / scale >= MainActivity.instance.width || options.outHeight / scale >= MainActivity.instance.height) {
                scale *= 2;
            }
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return BitmapFactory.decodeFile(pathName, options);
    }





    //此方法用于释放bitmap

    public static  void recyleBitmap(Bitmap bitmap){
        if(bitmap!=null&&!bitmap.isRecycled()){
            bitmap.recycle();
            System.gc();
        }
    }

    //根据图片uri得到图片地址

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(Uri uri) {
        ContentResolver contentResolver = MainActivity.instance.getContentResolver();
        String myPath = null;
        Cursor cursor = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT&&DocumentsContract.isDocumentUri(MainActivity.instance, uri)) {
                String wholeID = DocumentsContract.getDocumentId(uri);
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
                        sel, new String[]{id}, null);
                int columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    myPath = cursor.getString(columnIndex);
                }
            } else {
                String[] projection = {MediaStore.Images.Media.DATA};
                cursor = contentResolver.query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                myPath = cursor.getString(column_index);
            }
        }finally {
            if(cursor!=null)
                cursor.close();
            System.gc();
        }
        return myPath;
    }


    //裁剪图片，根据地址裁剪图片后返回图片的uri
    public static void cropImageUri(String path) {
        try {
            File file = new File(path);
            Intent intent = new Intent("com.android.camera.action.CROP");
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
//            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 198);
            intent.putExtra("outputY", 198);
            intent.putExtra("scale","true");
            intent.putExtra("circleCrop","true");
            // 设置为true直接返回bitmap
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, GloableUtils.imageUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            MainActivity.instance.startActivityForResult(intent, GloableUtils.CROP_A_PICTURE);

        }catch (Exception e){

            e.printStackTrace();
        }
    }
}
