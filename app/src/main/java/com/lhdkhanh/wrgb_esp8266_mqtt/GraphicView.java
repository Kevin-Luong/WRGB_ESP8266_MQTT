package com.lhdkhanh.wrgb_esp8266_mqtt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GraphicView extends View  {

    private Paint paint;
    public GraphicView(Context context) {
        //this(context, null);
        super(context);
    }

    public GraphicView(Context context, AttributeSet attrs) {
        //this(context, attrs, 0);
        super(context,attrs);
        initPaint();
    }

    public GraphicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw component in here
        //Draw Point
        float x = 100;
        float y = 200;
        float r = 30;
        canvas.drawCircle(x, y, r, paint);
        canvas.drawLine(100,100,200,100, paint);
    }



    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(4);
    }

}


