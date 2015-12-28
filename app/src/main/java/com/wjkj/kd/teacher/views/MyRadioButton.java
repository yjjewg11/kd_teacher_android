package com.wjkj.kd.teacher.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RadioButton;
public class MyRadioButton extends RadioButton {
    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static boolean isMessage = false;
    public static int widthSize ;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        if(isMessage){
            paint.setColor(Color.RED);
        }else{
            paint.setColor(Color.WHITE);
        }

        RectF rectF = new RectF(widthSize,5,widthSize+15,20);
        canvas.drawOval(rectF,paint);
    }

    public void canvasAgain(){
        invalidate();
    }
}
