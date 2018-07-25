package com.uvstudio.him.noteproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class RecyclerImgAdapter extends RecyclerView.Adapter<RecyclerImgAdapter.ImgViewHolder>{

    private ArrayList<ImageContent> icontent=new ArrayList<>();
    private Context con;
   // private static final String TAG="DRAW";
    public RecyclerImgAdapter(ArrayList<ImageContent> icontent, Context con) {
        this.icontent = icontent;
        this.con = con;
    }

    @Override
    public ImgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(con).inflate(R.layout.imgloader_card,parent,false);
        ImgViewHolder ihol=new ImgViewHolder(view);
        return ihol;
    }

    @Override
    public void onBindViewHolder(ImgViewHolder holder, int position) {
        final ImageContent icon=icontent.get(position);
       // Log.d(TAG, "onBindViewHolder: "+icon.getImgpath());
        Glide.with(con).load(icon.getImgpath()).into(holder.imageView);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                FragmentManager manager=((AppCompatActivity)con).getSupportFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                DrawFragment fragment=new DrawFragment();
                //ImgFolLoader_frag fragment=new ImgFolLoader_frag();
                Bundle bundle=new Bundle();
                bundle.putString("image",icontent.get(position).getImgpath().toString());
                bundle.putInt("key",25);
                fragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container,fragment).addToBackStack(null);
                transaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return icontent.size();
    }


    public static class ImgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imageView;
        ItemClickListener itemClickListener;
        public ImgViewHolder(View v)
        {
            super(v);
            imageView= (ImageView) v.findViewById(R.id.loadercard_imgview);
            imageView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

    }
}
