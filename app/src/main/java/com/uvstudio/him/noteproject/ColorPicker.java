package com.uvstudio.him.noteproject;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.util.ArrayList;


public class ColorPicker {
    Button dis;
    ImageView colrpicker,colordisplay;
    private static final String TAG="DRAW";
    int r,g,b;
    Bitmap bitmap;
    float hratio,wratio;
    int A=255;

    public ColorPicker( ImageView colrpicker, ImageView colordisplay) {
        this.colrpicker=colrpicker;
        bitmap =  Bitmap.createBitmap(((BitmapDrawable) colrpicker.getDrawable()).getBitmap());
        this.colordisplay = colordisplay;
        hratio=(float)bitmap.getHeight()/(float)colrpicker.getHeight();
        wratio=(float)bitmap.getWidth()/(float)colrpicker.getWidth();
    }

    public void extractRGB()
    {

        colrpicker.setOnTouchListener(new View.OnTouchListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    Matrix inverse=new Matrix();
                    ((ImageView)v).getImageMatrix().invert(inverse);
                    float[] touchpioint=new float[]{event.getX(),event.getY()};
                    inverse.mapPoints(touchpioint);
                    int x= (int)touchpioint[0];
                    int y= (int)touchpioint[1];
                   // int ximage=(int)(x*wratio);
                   // int yimage=(int)(y*hratio);
                    int pixel= bitmap.getPixel(x,y);

                    r=Color.red(pixel);
                    b=Color.blue(pixel);
                    g=Color.green(pixel);

                }
                colordisplay.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(r,g,b)));
               // Log.d(TAG, "onTouch: "+r+","+g+","+b);
                return true;
            }

        });

      //  colrpicker.performClick();

    }


    public ArrayList<Integer> getColor()
    {
        ArrayList<Integer> list=new ArrayList<>();
       // int color = (A & 0xff) << 24 | (r & 0xff) << 16 | (g & 0xff) << 16 | (b & 0xff);
        Log.d(TAG, "getColor "+r+","+g+","+b);
      /*  Log.d(TAG, "getColor: A "+((color>>24) & 0xff));
        Log.d(TAG, "getColor: R "+((color>>16) & 0xff));
        Log.d(TAG, "getColor: G "+((color>>16) & 0xff));
        Log.d(TAG, "getColor: B "+((color) & 0xff)); */
        list.add(r);
        list.add(g);
        list.add(b);
     //   dis.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,r,g,b)));
        return list;
    }
}
