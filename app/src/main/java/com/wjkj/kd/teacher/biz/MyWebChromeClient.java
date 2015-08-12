package com.wjkj.kd.teacher.biz;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.wjkj.kd.teacher.MainActivity;

public class MyWebChromeClient extends WebChromeClient {
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        MainActivity.instance.myUploadMsg = uploadMsg;
    }

}
