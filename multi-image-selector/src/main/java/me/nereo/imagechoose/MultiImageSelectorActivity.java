package me.nereo.imagechoose;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.nereo.imagechoose.bean.Image;
import me.nereo.imagechoose.utils.Utils;
import me.nereo.multi_image_selector.R;

/**
 * 多图选择
 * Created by Nereo on 2015/4/7.
 */
public class MultiImageSelectorActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback{

    /** 最大图片选择次数，int类型，默认9 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，默认多选 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，默认显示 */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    public static final String EXTRA_SHOW_TEXT = "show_text";
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "select_result";
    /** 默认选择集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<>();
    private Button mSubmitButton;
    private int mDefaultCount;
    private DisplayMetrics dm = new DisplayMetrics();

    public DisplayMetrics getDm() {
        return dm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_activity_default);
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 0);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        boolean isShowText = intent.getBooleanExtra(EXTRA_SHOW_TEXT, true);
        if(mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShowCamera);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_TEXT, isShowText);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle),"pic")
                .commit();

        // 返回按钮
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 完成按钮
        mSubmitButton = (Button) findViewById(R.id.commit);
        mSubmitButton.setEnabled(true);
        if(resultList == null || resultList.size()<=0){
            mSubmitButton.setText("完成");
        }else{
            if(mDefaultCount > 0){
                mSubmitButton.setText("完成("+resultList.size()+"/"+mDefaultCount+")");
            }else {
                mSubmitButton.setText("完成("+resultList.size()+")");
            }

        }
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiImageSelectorFragment fragment = (MultiImageSelectorFragment)
                        getSupportFragmentManager().findFragmentByTag("pic");
                if(fragment != null && fragment.getNewImageAdapter().getmSelectedImages().size() > 0){
                    List<Image> list =  fragment.getNewImageAdapter().getmSelectedImages();
                    Iterator<Image> iterator = list.iterator();
                    while (iterator.hasNext()){
                        String path = iterator.next().getPath();
                        if (Utils.stringIsNull(path)) continue;
                        resultList.add(path);
                    }
                }else {
                    return;
                }
                if(resultList != null && resultList.size() >0){
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状态
        if(resultList.size() > 0){
            mSubmitButton.setText("完成("+resultList.size()+")");
            if(!mSubmitButton.isEnabled()){
                mSubmitButton.setEnabled(true);
            }
        }
    }
    public void showPic(List<Image> list){
            mSubmitButton.setText("完成("+list.size()+")");
            if(!mSubmitButton.isEnabled()){
                mSubmitButton.setEnabled(true);
            }
    }
    public void giveAll(){
        resultList.clear();
        if(resultList.size() > 0){
            mSubmitButton.setText("完成("+resultList.size()+")");
            if(!mSubmitButton.isEnabled()){
                mSubmitButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
            mSubmitButton.setText("完成("+resultList.size()+")");
        }else{
            mSubmitButton.setText("完成("+resultList.size()+")");
        }
        // 当为选择图片时候的状态
        if(resultList.size() == 0){
            mSubmitButton.setText("完成");
            mSubmitButton.setEnabled(true);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if(imageFile != null) {
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
