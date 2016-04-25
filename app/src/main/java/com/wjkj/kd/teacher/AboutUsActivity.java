package com.wjkj.kd.teacher;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutUsActivity extends BaseActivity {

    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        image = (ImageView)findViewById(R.id.imageViewbackrrow);
        image.setOnClickListener(this);
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.imageViewbackrrow:
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
