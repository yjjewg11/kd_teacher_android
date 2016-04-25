package me.nereo.multiimageselector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import me.nereo.imagechoose.MultiImageSelectorActivity;
import me.nereo.imagechoose.utils.Utils;
import song.image.crop.HDApp;


public class MainActivity extends Activity {

    private static final int REQUEST_IMAGE = 2;

    private TextView mResultText;
    private RadioGroup mChoiceMode, mShowCamera,mShowText;
    private EditText mRequestNum;
    private ImageView iv_result;
    public static String RESOLVE_IMAGE = "resolve_image";
    public  ArrayList<String> mSelectPath;
    private Intent intent;
    private static MainActivity instancefinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instancefinish = this;
        Log.i("TAG", "打印对象的引用" + instancefinish);
        mResultText = (TextView) findViewById(R.id.result);
        mChoiceMode = (RadioGroup) findViewById(R.id.choice_mode);
        mShowCamera = (RadioGroup) findViewById(R.id.show_camera);
        mShowText = (RadioGroup) findViewById(R.id.show_text_group);
        mRequestNum = (EditText) findViewById(R.id.request_num);
        iv_result = (ImageView) findViewById(R.id.iv_result);

        mChoiceMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.multi){
                    mRequestNum.setEnabled(true);
                }else{
                    mRequestNum.setEnabled(false);
                    mRequestNum.setText("");
                }
            }
        });



                int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

                if (mChoiceMode.getCheckedRadioButtonId() == R.id.single) {
                    selectedMode = MultiImageSelectorActivity.MODE_SINGLE;
                } else {
                    selectedMode = MultiImageSelectorActivity.MODE_MULTI;
                }

                boolean showCamera = mShowCamera.getCheckedRadioButtonId() == R.id.show;
                boolean showText = mShowText.getCheckedRadioButtonId() == R.id.show_text;

                int maxNum = 0;
                if (!TextUtils.isEmpty(mRequestNum.getText())) {
                    maxNum = Integer.valueOf(mRequestNum.getText().toString());
                }
                String  count = getIntent().getStringExtra("maxCount");
                if(!Utils.stringIsNull(count)){
                  if(Integer.valueOf(count) > 0){
                    maxNum = Integer.valueOf(count);
                  }
                }
                intent = new Intent(MainActivity.this, MultiImageSelectorActivity.class);
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);// 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_TEXT, showText);//显示文本
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);// 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode); // 选择模式
                if (mSelectPath != null && mSelectPath.size() > 0) {// 回显已经选择的图片
//                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }

        startActivityForResult(intent, REQUEST_IMAGE);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
//                for(String p: mSelectPath){
//                    sb.append(p);
//                    sb.append("\n");
//                }
//                mResultText.setText(sb.toString());
                //获得图片地址后发送广播处理
                Intent intent = new Intent();
                intent.putExtra("imageList",mSelectPath);
                intent.setAction(RESOLVE_IMAGE);
                sendBroadcast(intent);
                finish();
            }else{
                //如果请求码为取消，则关闭页面
                if(resultCode==RESULT_CANCELED) {
                    Log.i("TAG","打印结果码"+resultCode);
                    try {
                        instancefinish.finish();
                        Log.i("TAG","打印MainActivity.this"+MainActivity.this);
                        return;
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }
                }

                if(HDApp.getInstance().getSingleChooseFile()!=null && HDApp.getInstance().getSingleChooseFile().getTotalSpace()>0){
                    Bitmap loacalBitmap = getLoacalBitmap(HDApp.getInstance().getSingleChooseFile());
                    iv_result.setImageBitmap(loacalBitmap);
                    HDApp.getInstance().setSingleChooseFile(null);

                    Toast.makeText(this, "裁切完成" + iv_result.getWidth() + "  " + iv_result.getHeight(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "裁切完成 空文件" , Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public static Bitmap getLoacalBitmap(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.i("TAG","销毁方法已经被调用");
        super.onDestroy();
    }
}
