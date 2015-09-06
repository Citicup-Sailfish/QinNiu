package com.qinniuclient.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by TanXiao on 2015/9/6.
 */
public class MyCircleView extends View {
    public MyCircleView(Context context) {
        super(context);
    }

    public MyCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawCircle(40, 40, 30, paint);
    }
}
