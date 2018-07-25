package com.uvstudio.him.noteproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class CustomView extends View{
    Path path;
    Paint dPaint,cPaint;
    Canvas canvas;
    Bitmap bitmap,ibmp;
    int color,r,g,b;
    private static final String TAG="DRAW";
   public CustomView(Context context, int color) {
        super(context);
        this.color = color;
        r=(color>>16)&0xff;
        g=(color>>16)&0xff;
        b=((color)&0xff);
        Log.d(TAG, "CustomView: "+r+g+b);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        path=new Path();
        dPaint=new Paint();
        dPaint.setStrokeWidth(10);
        dPaint.setStyle(Paint.Style.STROKE);
        dPaint.setStrokeCap(Paint.Cap.ROUND);
        dPaint.setStrokeJoin(Paint.Join.ROUND);
      //  dPaint.setColor(Color.argb(255,r,g,b));
        cPaint=new Paint(Paint.DITHER_FLAG);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,0,0,cPaint);
        canvas.drawPath(path,dPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // X and Y position of user touch.
        float touchX = event.getX();
        float touchY = event.getY();
        // Draw the path according to the touch event taking place.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:

                canvas.drawPath(path, dPaint);
                path.reset();
                dPaint.setXfermode(null);
                break;
            default:
                return false;
        }

        // invalidate the view so that canvas is redrawn.
        invalidate();
        return true;
    }
    public void layView(){
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }
    public void setColor(int r,int g,int b){
        int RGB = android.graphics.Color.argb(255,r, g, b);
        dPaint.setColor(RGB);
    }
    public void setStokeWidth(float width){
        dPaint.setStrokeWidth(width);
    }
    public void setImageView(Bitmap bmp)
    {
        setBackgroundDrawable(new BitmapDrawable(bmp));
    }
}
