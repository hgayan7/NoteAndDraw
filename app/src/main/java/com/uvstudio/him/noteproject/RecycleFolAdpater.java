package com.uvstudio.him.noteproject;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.util.ArrayList;


public class RecycleFolAdpater extends RecyclerView.Adapter<RecycleFolAdpater.ViewHolder> {
    private ArrayList<DirContent> dcon =new ArrayList<>();
    private Context context;
   // private static final String TAG="DRAW";

    public RecycleFolAdpater(ArrayList<DirContent> dcon, Context context) {
        this.dcon = dcon;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.imgfol_card,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DirContent con=dcon.get(position);
        holder.dis.setText(con.getPath());
        ArrayList<File> file=imgdir(con.getDirpath());
       // Log.d(TAG, "onBindViewHolder: "+file.size());
        Glide.with(context).load(file.get(0)).into(holder.img);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                FragmentManager manager=((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction trans=manager.beginTransaction();
                ImgLoader_Frag f=new ImgLoader_Frag();
                Bundle bun=new Bundle();
                bun.putString("imgpath",con.getDirpath().toString());
                f.setArguments(bun);
                trans.replace(R.id.fragment_container,f).addToBackStack(null);
                trans.commit();
            }
        });

    }

    public ArrayList<File> imgdir(File root)
    {
        ArrayList<File> imgpath=new ArrayList<>();
        File[] files=root.listFiles();
        for(int i=0;i<files.length;i++)
        {
            if (files[i].isDirectory()) {
                imgpath.addAll(imgdir(files[i]));
            }else
            {
                if(files[i].getName().endsWith(".jpg") || files[i].getName().endsWith(".png"))
                {
                    imgpath.add(files[i]);
                }
            }
        }

        return imgpath;
    }

    @Override
    public int getItemCount() {
        return dcon.size();
    }

    public static  class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener, View.OnLongClickListener
    {
        ImageView img;
        TextView dis;
        ItemClickListener itemClickListener;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public ViewHolder(View itemView) {
            super(itemView);
            img= (ImageView) itemView.findViewById(R.id.cardfol_img);
            dis= (TextView) itemView.findViewById(R.id.cardfol_path);
            dis.getBackground().setAlpha(100);
            img.setOnClickListener(this);
            img.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }
}
