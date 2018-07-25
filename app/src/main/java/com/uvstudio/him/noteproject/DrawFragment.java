package com.uvstudio.him.noteproject;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import android.icu.util.Measure;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class DrawFragment extends Fragment implements View.OnClickListener{
    CustomView customView;
    com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton;
    private Context context;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE =1;
    private static final String TAG="DRAW";
    private Button drawclick,selectcolor,stroke,mcolor,eraser;
    private   FloatingActionMenu actionMenu;
    private SaveImage simg;
    private ColorPicker picker;
    private ArrayList<Integer> color;
    private LinearLayout hidelay;
    ImageView cpicker,cdisplay;
    private ImgAnimator anim=new ImgAnimator();
    private float strokewidth;
    ImageButton one,two,three,four,five,six;
    public DrawFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_draw, container, false);
        customView=(CustomView)view.findViewById(R.id.customView);
        drawclick= (Button) view.findViewById(R.id.saveOnDraw);
        stroke= (Button) view.findViewById(R.id.strokesize);
        mcolor= (Button) view.findViewById(R.id.color);
        selectcolor= (Button) view.findViewById(R.id.okcolor);
        hidelay= (LinearLayout) view.findViewById(R.id.drawHide);

        cpicker= (ImageView) view.findViewById(R.id.colorviewer);
        cdisplay= (ImageView) view.findViewById(R.id.colordisplay);
      //  Glide.with(this).load(R.drawable.color).into(cpicker);
        cpicker.setImageResource(R.drawable.color);
        picker=new ColorPicker(cpicker,cdisplay);
        picker.extractRGB();
        selectcolor.setOnClickListener(this);
        stroke.setOnClickListener(this);
        mcolor.setOnClickListener(this);
        drawclick.setOnClickListener(this);
        one=(ImageButton)view.findViewById(R.id.StrokeOne);
        two=(ImageButton)view.findViewById(R.id.StrokeTwo);
        three=(ImageButton)view.findViewById(R.id.StrokeThree);
        four=(ImageButton)view.findViewById(R.id.Strokefour);
        five=(ImageButton)view.findViewById(R.id.Strokefive);
        six=(ImageButton)view.findViewById(R.id.Strokesix);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        eraser=(Button)view.findViewById(R.id.eraser);
        eraser.setOnClickListener(this);
      /*  drawclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Bitmap bmp=Bitmap.createBitmap()
                SimpleDateFormat sdf=new SimpleDateFormat("hhmmss");
                String filename="NoteApp"+sdf.format(new Date())+".png";
                simg=new SaveImage(context);
                 ProgressDialog dialog=ProgressDialog.show(getActivity(),"","Saving...",true);
                simg.savetosdcard(convertToBitmap(customView),filename);
                simg.startScan();
                dialog.dismiss();
                Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show();
            }
        }); */
        setCustomViewImage(customView);

       // getCamImage(customView);
        // Create an icon
       ImageView icon = new ImageView(getActivity());
        icon.setImageResource(R.drawable.add);
        // https://github.com/oguzbilgener/CircularFloatingActionMenu
        actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(getActivity())
                .setContentView(icon).setLayoutParams(new
                        FloatingActionButton.LayoutParams(120,120)).setPosition(2).setBackgroundDrawable(R.drawable.cusbutton)
                .build();
        actionButton.setVisibility(View.VISIBLE);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());

        // repeat many times:
        ImageView itemIcon1 = new ImageView(getActivity());
        itemIcon1.setImageResource(R.drawable.whiteboard);

        ImageView itemIcon2 = new ImageView(getActivity());
        itemIcon2.setImageResource(R.drawable.image);

        ImageView itemIcon3 = new ImageView(getActivity());
        itemIcon3.setImageResource(R.drawable.camera);

        SubActionButton button1 = itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.whiteboard)).
                setLayoutParams(new FloatingActionButton.LayoutParams(100,100)).build();
        SubActionButton button2 = itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.image)).
                setLayoutParams(new FloatingActionButton.LayoutParams(100,100)).build();
        SubActionButton button3 = itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.camera)).
                setLayoutParams(new FloatingActionButton.LayoutParams(100,100)).build();

        //attach the sub buttons to the main button
        actionMenu = new FloatingActionMenu.Builder(getActivity())
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                .attachTo(actionButton)
                .setStartAngle(90).setEndAngle(180).setRadius(250)
                .build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.setVisibility(View.VISIBLE);
                customView.layView();
                actionMenu.close(true);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.setVisibility(View.INVISIBLE);
                 manageClick();
               // openGallery();
                actionMenu.close(true);
            }
        });

   /*     selectcolor.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                color=picker.getColor();
                Log.d(TAG, "onClick: "+color.get(0)+","+color.get(1)+","+color.get(2));
                anim.exitReveal(hidelay,(int)v.getY(),(int)v.getX());
            }
        });

        stroke.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                anim.enterReveal(hidelay,(int)v.getX(),(int)v.getY());
            }
        }); */
        return view;
    }


    ////////////////////////////////////////////////////////////
   public void manageClick()
     {
         if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, PERMISSION_READ_EXTERNAL_STORAGE);
         } else {
             openGallery();

         }
     }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch(requestCode)
        {
            case PERMISSION_READ_EXTERNAL_STORAGE:{
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    openGallery();
                }else
                    Toast.makeText(context,"Failed!",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void openGallery()
    {
        FragmentManager fm=((AppCompatActivity)context).getSupportFragmentManager();
        FragmentTransaction tran=fm.beginTransaction();
        ImgFolLoader_frag frag=new ImgFolLoader_frag();
        tran.replace(R.id.fragment_container,frag).addToBackStack(null);
        tran.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       actionButton.setVisibility(View.GONE);
       actionMenu.close(true);
    }
////////////////////////////////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setCustomViewImage(CustomView view)
    {
        try {
            int key=getArguments().getInt("key");
            if(key==25)
            {
                String path = getArguments().getString("image");
                Bitmap image=BitmapFactory.decodeFile(path);
               // Log.d(TAG, "setCustomViewImage: "+key);
                view.setBackground(new BitmapDrawable(image));
                view.setVisibility(View.VISIBLE);
            }else if(key==122)
            {
                WindowManager wm= (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm=new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                String camimage=getArguments().getString("camImage");
              //  Log.d(TAG, "setCustomViewImage: "+camimage);
                Bitmap bmp=BitmapFactory.decodeFile(camimage);
                //Bitmap newbmp=scaler.decodeScaledBitmapFromSdCard(1,camimage,wm.getDefaultDisplay().getWidth(),wm.getDefaultDisplay().getHeight());
                Bitmap newbmp=Bitmap.createScaledBitmap(bmp,wm.getDefaultDisplay().getWidth(),wm.getDefaultDisplay().getHeight(),false);
                view.setBackground(new BitmapDrawable(newbmp));
                view.setVisibility(View.VISIBLE);
            }

        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }
//////////////////////////////////////////////////////////////////


    private Bitmap convertToBitmap(CustomView view)
    {
        int width=view.getWidth();
        int height=view.getHeight();
        Bitmap bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas c=new Canvas(bitmap);
        view.draw(c);
        return  bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.okcolor:
                color=picker.getColor();
                customView.setColor(color.get(0),color.get(1),color.get(2));
                Log.d(TAG, "onClick: "+color.get(0)+","+color.get(1)+","+color.get(2));
                anim.exitReveal(hidelay,(int)v.getY(),(int)v.getX());
                break;
            case R.id.strokesize:
                anim.enterReveal(hidelay,(int)v.getX(),(int)v.getY());
                break;
            case R.id.color:
                anim.enterReveal(hidelay,(int)v.getX(),(int)v.getY());

                break;
            case R.id.saveOnDraw:
                SimpleDateFormat sdf=new SimpleDateFormat("hhmmss");
                String filename="NoteApp"+sdf.format(new Date())+".png";
                simg=new SaveImage(context);
                ProgressDialog dialog=ProgressDialog.show(getActivity(),"","Saving...",true);
                simg.savetosdcard(convertToBitmap(customView),filename);
                simg.startScan();
                dialog.dismiss();
                Toast.makeText(context,"Saved",Toast.LENGTH_SHORT).show();
                break;
            case R.id.StrokeOne:
                strokewidth=5.0f;
                customView.setStokeWidth(strokewidth);
                one.setBackground(getResources().getDrawable(R.drawable.stroke1));
                two.setBackground(getResources().getDrawable(R.drawable.stroke));
                three.setBackground(getResources().getDrawable(R.drawable.stroke));
                four.setBackground(getResources().getDrawable(R.drawable.stroke));
                five.setBackground(getResources().getDrawable(R.drawable.stroke));
                six.setBackground(getResources().getDrawable(R.drawable.stroke));
                break;
            case R.id.StrokeTwo:
                strokewidth=10.0f;
                customView.setStokeWidth(strokewidth);
                one.setBackground(getResources().getDrawable(R.drawable.stroke));
                two.setBackground(getResources().getDrawable(R.drawable.stroke1));
                three.setBackground(getResources().getDrawable(R.drawable.stroke));
                four.setBackground(getResources().getDrawable(R.drawable.stroke));
                five.setBackground(getResources().getDrawable(R.drawable.stroke));
                six.setBackground(getResources().getDrawable(R.drawable.stroke));
                break;
            case R.id.StrokeThree:
                strokewidth=15.0f;
                customView.setStokeWidth(strokewidth);
                one.setBackground(getResources().getDrawable(R.drawable.stroke));
                two.setBackground(getResources().getDrawable(R.drawable.stroke));
                three.setBackground(getResources().getDrawable(R.drawable.stroke1));
                four.setBackground(getResources().getDrawable(R.drawable.stroke));
                five.setBackground(getResources().getDrawable(R.drawable.stroke));
                six.setBackground(getResources().getDrawable(R.drawable.stroke));
                break;
            case R.id.Strokefour:
                strokewidth=20.0f;
                customView.setStokeWidth(strokewidth);
                one.setBackground(getResources().getDrawable(R.drawable.stroke));
                two.setBackground(getResources().getDrawable(R.drawable.stroke));
                three.setBackground(getResources().getDrawable(R.drawable.stroke));
                four.setBackground(getResources().getDrawable(R.drawable.stroke1));
                five.setBackground(getResources().getDrawable(R.drawable.stroke));
                six.setBackground(getResources().getDrawable(R.drawable.stroke));
                break;
            case R.id.Strokefive:
                strokewidth=25.0f;
                customView.setStokeWidth(strokewidth);
                one.setBackground(getResources().getDrawable(R.drawable.stroke));
                two.setBackground(getResources().getDrawable(R.drawable.stroke));
                three.setBackground(getResources().getDrawable(R.drawable.stroke));
                four.setBackground(getResources().getDrawable(R.drawable.stroke));
                five.setBackground(getResources().getDrawable(R.drawable.stroke1));
                six.setBackground(getResources().getDrawable(R.drawable.stroke));
                break;
            case R.id.Strokesix:
                strokewidth=30.0f;
                customView.setStokeWidth(strokewidth);
                one.setBackground(getResources().getDrawable(R.drawable.stroke));
                two.setBackground(getResources().getDrawable(R.drawable.stroke));
                three.setBackground(getResources().getDrawable(R.drawable.stroke));
                four.setBackground(getResources().getDrawable(R.drawable.stroke));
                five.setBackground(getResources().getDrawable(R.drawable.stroke));
                six.setBackground(getResources().getDrawable(R.drawable.stroke1));
                break;
            case R.id.eraser:
                customView.layView();
                break;

        }
    }
}
