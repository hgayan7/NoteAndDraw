package com.uvstudio.him.noteproject;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class RecycleImgViewer extends RecyclerView.Adapter<RecycleImgViewer.ImgViewHolder> {

    Context context;
    ArrayList<ImageContent> content=new ArrayList<>();

    ViewerListner viewerListner;
    public RecycleImgViewer(Context context, ArrayList<ImageContent> content,ViewerListner viewerListner) {
        this.context = context;
        this.content = content;
        this.viewerListner=viewerListner;
    }

    @Override
    public ImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.imgloader_card,parent,false);
        ImgViewHolder hold=new ImgViewHolder(view);
        return hold;
    }

    @Override
    public void onBindViewHolder(ImgViewHolder holder, int position) {
        final ImageContent img=content.get(position);
        Glide.with(context).load(img.getImgpath()).into(holder.display);
        holder.display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("DRAW", "onClick: "+"worked!");
                viewerListner.onView(img.getImgpath().toString());
            }
        });
    }



    @Override
    public int getItemCount() {
        return content.size();
    }

    public  class ImgViewHolder extends RecyclerView.ViewHolder
    {
        //ItemClickListener itemClickListener;
        ImageView display;
        public ImgViewHolder(View itemView) {
            super(itemView);
            display= (ImageView) itemView.findViewById(R.id.loadercard_imgview);

            //display.setOnClickListener(this);

        }

      /*  public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);

        }*/
    }


}
