package com.uvstudio.him.noteproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class ImgLoader_Frag extends Fragment {
    private Context context;
    ArrayList<ImageContent> imgcon=new ArrayList<>();
    private ArrayList<File> imglist;
    private static final String TAG="DRAW";
    private RecyclerView irecy;
    private RecyclerImgAdapter adapter;
    public ImgLoader_Frag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_img_loader, container, false);
        Toast.makeText(context,"ImgLoaderFragment",Toast.LENGTH_SHORT).show();

        Bundle bun=getArguments();
        String path=bun.getString("imgpath");
        //Log.d(TAG, "onCreateView: "+path);
        File ipath=new File(path);
        imglist=imagelist(ipath);

        for(int i=0;i<imglist.size();i++)
        {
            ImageContent icon=new ImageContent(imglist.get(i));
            imgcon.add(icon);
           // Log.d(TAG, "onCreateView: "+imglist.get(i).getAbsolutePath());
        }

        irecy= (RecyclerView) view.findViewById(R.id.imgloader_recycler);
        //RecyclerView.LayoutManager manager=new GridLayoutManager(context,2);
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        irecy.setHasFixedSize(true);
        adapter=new RecyclerImgAdapter(imgcon,context);
        irecy.setLayoutManager(manager);
        irecy.setAdapter(adapter);
        return view;
    }

    public ArrayList<File> imagelist(File iroot) // return array containing the image of corresponding folder
    {
        ArrayList<File> ilist=new ArrayList<>();
        File[] ifile=iroot.listFiles();
        for(int j=0;j<ifile.length;j++)
        {
            if(ifile[j].isDirectory())
            {
                ilist.addAll(imagelist((ifile[j])));
            }else
            {
                if(ifile[j].getName().endsWith(".png") || ifile[j].getName().endsWith(".jpg"))
                {
                    ilist.add(ifile[j]);
                }
            }
        }
        return ilist;
    }
    @Override
    public void onAttach(Context context) {
        this.context=context;
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
