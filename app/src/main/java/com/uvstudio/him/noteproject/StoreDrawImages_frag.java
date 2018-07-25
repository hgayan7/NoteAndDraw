package com.uvstudio.him.noteproject;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.GestureLibraries;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.util.ArrayList;


public class StoreDrawImages_frag extends Fragment implements ViewerListner {


    com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton;
    Context context;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE =1;
    private static final int CAM_PERMISSION = 22;
    private static final int CAPTURE_IMAGE=100;
    private static final String TAG="DRAW";
    private RecyclerView saverec;
    File filepath;
    private ArrayList<ImageContent> ilist=new ArrayList<>();
    ArrayList<File> ifiles;
    private FloatingActionMenu actionMenu;
    SaveImage simg=new SaveImage(context);
    RelativeLayout animlay;
    ImageView hidenview;
    ImgAnimator anim=new ImgAnimator();
    String ipath;

    public StoreDrawImages_frag() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_draw_images, container, false);
        animlay = (RelativeLayout) view.findViewById(R.id.animlayout);
        hidenview = (ImageView) view.findViewById(R.id.focusimg);
        ImageView icon = new ImageView(getActivity());
        icon.setImageResource(R.drawable.add);
        actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(getActivity())
                .setContentView(icon).setLayoutParams(new
                        FloatingActionButton.LayoutParams(150, 150)).setPosition(2).setBackgroundDrawable(R.drawable.cusbutton)
                .build();
        actionButton.setVisibility(View.VISIBLE);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(getActivity());
        ImageView itemIcon1 = new ImageView(getActivity());
        itemIcon1.setImageResource(R.drawable.whiteboard);

        ImageView itemIcon2 = new ImageView(getActivity());
        itemIcon2.setImageResource(R.drawable.image);

        ImageView itemIcon3 = new ImageView(getActivity());
        itemIcon3.setImageResource(R.drawable.camera);

        SubActionButton button1 = itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.whiteboard)).
                setLayoutParams(new FloatingActionButton.LayoutParams(100, 100)).build();
        SubActionButton button2 = itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.image)).
                setLayoutParams(new FloatingActionButton.LayoutParams(100, 100)).build();
        SubActionButton button3 = itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.camera)).
                setLayoutParams(new FloatingActionButton.LayoutParams(100, 100)).build();

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
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                DrawFragment draw = new DrawFragment();
                transaction.replace(R.id.fragment_container, draw).commitNow();
                actionMenu.close(true);
                actionButton.setVisibility(View.INVISIBLE);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageClick();
                // openGallery();
                actionMenu.close(true);
                actionButton.setVisibility(View.INVISIBLE);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageCamera();
                actionMenu.close(true);
                actionButton.setVisibility(View.INVISIBLE);
            }

        });

        filepath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/NoteDraw");
        if (filepath.exists()) {

                ifiles = simg.imageReader(filepath);
                //  Log.d(TAG, "onCreateView: NoteDraw exists"+ifiles.get(0));


                for (int i = 0; i < ifiles.size(); i++) {
                    ImageContent content = new ImageContent(ifiles.get(i));
                    ilist.add(content);
                }

                saverec = (RecyclerView) view.findViewById(R.id.saveRecycler);
                StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                RecycleImgViewer adpater = new RecycleImgViewer(context, ilist, this);
                saverec.setLayoutManager(manager);
                saverec.setHasFixedSize(true);
                saverec.setAdapter(adpater);

                saverec.addOnItemTouchListener(new RecycleItemClickListener(context, new RecycleItemClickListener.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        ;
                        anim.enterReveal(animlay, (int) view.getX(), (int) view.getY());
                    }
                }));
                touchlistener(animlay);
            }


       /* view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
               // v.removeOnLayoutChangeListener(this);
                anim.enterReveal(v,(int)v.getX(),(int)v.getY());
            }
        }); */
        return view;
    }


    private void touchlistener(View v)
    {
        v.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked()==MotionEvent.ACTION_UP)
                {
                   // Log.d(TAG, "onTouch: "+String.valueOf(v.getId()));
                    anim.exitReveal(animlay,(int)event.getX(),(int)event.getY());
                }
                return true;
            }
        });
    }
//////////////////////////////////Gallery Stuffs
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
        tran.replace(R.id.fragment_container,frag);
        tran.commit();
    }
    ///////////////////////
    /////////////Camera Permission and Stuffs
    private void manageCamera() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, CAM_PERMISSION);
        } else {
            showCamera();
        }

    }

    public void showCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //mImgageUri =Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image"+String.valueOf(System.currentTimeMillis())+".jpg"));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageuri());
        startActivityForResult(cameraIntent, CAPTURE_IMAGE);
    }
    public Uri setImageuri() {
        File file = new File(Environment.getExternalStorageDirectory()+ "/DCIM/"+File.separator + "image.jpg");
        Uri imguri = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID+".provider",file);
        return imguri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==CAPTURE_IMAGE)
            {
                File filepath=new File(Environment.getExternalStorageDirectory()+"/DCIM/"+File.separator + "image.jpg");
                FragmentManager manager=((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                DrawFragment fragment=new DrawFragment();
                Bundle bun=new Bundle();
                bun.putString("camImage",filepath.getAbsolutePath());
                bun.putInt("key",122);
                fragment.setArguments(bun);
                transaction.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            }
        }
    }
    ///////////////////////////////
    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        actionButton.setVisibility(View.GONE);
        actionMenu.close(true);

    }


    @Override
    public void onView(String path) {
      //  Log.d(TAG, "onView: "+ipath);
        Bitmap bmp=BitmapFactory.decodeFile(path);
        hidenview.setImageBitmap(bmp);
    }
}
