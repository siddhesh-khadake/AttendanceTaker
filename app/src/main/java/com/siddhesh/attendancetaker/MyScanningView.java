package com.siddhesh.attendancetaker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class MyScanningView extends View
{
    private Paint[] paint;
    private int alpha=255;
    private int mPosY = 0;
    private int DELAY=0;
    private boolean runAnimation = true;
    private boolean showLine = true;
    private Handler handler;
    private Runnable refreshRunnable;
    private boolean isGoingDown = true;
    private int mHeight;
    int myColor;

    public MyScanningView(Context context) {
        super(context);
        init();
    }

    public MyScanningView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScanningView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        myColor = getResources().getColor(R.color.holo_blue_bright);
        paint = new Paint[25];
        for(int i=0;i<25;i++)
        {
            paint[i] = new Paint();
            paint[i].setColor(myColor);
            paint[i].setStrokeWidth(2.0f);
            paint[i].setAlpha(alpha);
            alpha=alpha-10;
        }
        //paint.setAlpha(100);
        //Add anything else you want to customize your line, like the stroke width
        handler = new Handler();
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                refreshView();
            }
        };
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        mHeight = canvas.getHeight();
        if (showLine)
        {
            if(isGoingDown) {
                canvas.drawLine(0, mPosY, canvas.getWidth(), mPosY, paint[0]);
                canvas.drawLine(0, mPosY - 4, canvas.getWidth(), mPosY - 4, paint[1]);
                canvas.drawLine(0, mPosY - 8, canvas.getWidth(), mPosY - 8, paint[2]);
                canvas.drawLine(0, mPosY - 12, canvas.getWidth(), mPosY - 12, paint[3]);
                canvas.drawLine(0, mPosY - 16, canvas.getWidth(), mPosY - 16, paint[4]);
                canvas.drawLine(0, mPosY - 20, canvas.getWidth(), mPosY - 20, paint[5]);
                canvas.drawLine(0, mPosY - 24, canvas.getWidth(), mPosY - 24, paint[6]);
                canvas.drawLine(0, mPosY - 28, canvas.getWidth(), mPosY - 28, paint[7]);
                canvas.drawLine(0, mPosY - 32, canvas.getWidth(), mPosY - 32, paint[8]);
                canvas.drawLine(0, mPosY - 36, canvas.getWidth(), mPosY - 36, paint[9]);
                canvas.drawLine(0, mPosY - 40, canvas.getWidth(), mPosY - 40, paint[10]);
                canvas.drawLine(0, mPosY - 44, canvas.getWidth(), mPosY - 44, paint[11]);
                canvas.drawLine(0, mPosY - 48, canvas.getWidth(), mPosY - 48, paint[12]);
                canvas.drawLine(0, mPosY - 52, canvas.getWidth(), mPosY - 52, paint[13]);
                canvas.drawLine(0, mPosY - 56, canvas.getWidth(), mPosY - 56, paint[14]);
                canvas.drawLine(0, mPosY - 60, canvas.getWidth(), mPosY - 60, paint[15]);
                canvas.drawLine(0, mPosY - 64, canvas.getWidth(), mPosY - 64, paint[16]);
                canvas.drawLine(0, mPosY - 68, canvas.getWidth(), mPosY - 68, paint[17]);
                canvas.drawLine(0, mPosY - 72, canvas.getWidth(), mPosY - 72, paint[18]);
                canvas.drawLine(0, mPosY - 76, canvas.getWidth(), mPosY - 76, paint[19]);
                canvas.drawLine(0, mPosY - 80, canvas.getWidth(), mPosY - 80, paint[20]);
                canvas.drawLine(0, mPosY - 84, canvas.getWidth(), mPosY - 84, paint[21]);
                canvas.drawLine(0, mPosY - 88, canvas.getWidth(), mPosY - 88, paint[22]);
                canvas.drawLine(0, mPosY - 92, canvas.getWidth(), mPosY - 92, paint[23]);
                canvas.drawLine(0, mPosY - 96, canvas.getWidth(), mPosY - 96, paint[24]);
            }
            else
            {
                canvas.drawLine(0, mPosY, canvas.getWidth(), mPosY, paint[0]);
                canvas.drawLine(0, mPosY+4, canvas.getWidth(), mPosY+4, paint[1]);
                canvas.drawLine(0, mPosY+8, canvas.getWidth(), mPosY+8, paint[2]);
                canvas.drawLine(0, mPosY+12, canvas.getWidth(), mPosY+12, paint[3]);
                canvas.drawLine(0, mPosY+16, canvas.getWidth(), mPosY+16, paint[4]);
                canvas.drawLine(0, mPosY+20, canvas.getWidth(), mPosY+20, paint[5]);
                canvas.drawLine(0, mPosY+24, canvas.getWidth(), mPosY+24, paint[6]);
                canvas.drawLine(0, mPosY+28, canvas.getWidth(), mPosY+28, paint[7]);
                canvas.drawLine(0, mPosY+32, canvas.getWidth(), mPosY+32, paint[8]);
                canvas.drawLine(0, mPosY+36, canvas.getWidth(), mPosY+36, paint[9]);
                canvas.drawLine(0, mPosY+40, canvas.getWidth(), mPosY+40, paint[10]);
                canvas.drawLine(0, mPosY+44, canvas.getWidth(), mPosY+44, paint[11]);
                canvas.drawLine(0, mPosY+48, canvas.getWidth(), mPosY+48, paint[12]);
                canvas.drawLine(0, mPosY+52, canvas.getWidth(), mPosY+52, paint[13]);
                canvas.drawLine(0, mPosY+56, canvas.getWidth(), mPosY+56, paint[14]);
                canvas.drawLine(0, mPosY+60, canvas.getWidth(), mPosY+60, paint[15]);
                canvas.drawLine(0, mPosY+64, canvas.getWidth(), mPosY+64, paint[16]);
                canvas.drawLine(0, mPosY+68, canvas.getWidth(), mPosY+68, paint[17]);
                canvas.drawLine(0, mPosY+72, canvas.getWidth(), mPosY+72, paint[18]);
                canvas.drawLine(0, mPosY+76, canvas.getWidth(), mPosY+76, paint[19]);
                canvas.drawLine(0, mPosY+80, canvas.getWidth(), mPosY+80, paint[20]);
                canvas.drawLine(0, mPosY+84, canvas.getWidth(), mPosY+84, paint[21]);
                canvas.drawLine(0, mPosY+88, canvas.getWidth(), mPosY+88, paint[22]);
                canvas.drawLine(0, mPosY+92, canvas.getWidth(), mPosY+92, paint[23]);
                canvas.drawLine(0, mPosY+96, canvas.getWidth(), mPosY+96, paint[24]);
            }

        }
        if (runAnimation)
        {
            handler.postDelayed(refreshRunnable, DELAY);
        }
    }

    public void startAnimation()
    {
        runAnimation = true;
        showLine = true;
        this.invalidate();
    }

    public void stopAnimation()
    {
        runAnimation = false;
        showLine = false;
        reset();
        this.invalidate();
    }

    private void reset()
    {
        mPosY = 0;
        isGoingDown = true;


    }

    private void refreshView() {
        //Update new position of the line
        if (isGoingDown) {
            mPosY += 5;
            if (mPosY > mHeight) {
                mPosY = mHeight;
                isGoingDown = false;
            }
        } else {
            //We invert the direction of the animation
            mPosY -= 5;
            if (mPosY < 0) {
                mPosY = 0;
                isGoingDown = true;
            }
        }
        this.invalidate();
    }
}