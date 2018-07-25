package com.uvstudio.him.noteproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class ImgFolLoader_frag extends Fragment {
    private  Context con;
    private File rootdir;
    private ArrayList<File> dirpath;
    ArrayList<DirContent> content=new ArrayList<>();
    private RecyclerView recyclerView;
    public ImgFolLoader_frag() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        con=context;
        super.onAttach(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_img_fol_loader, container, false);
        recyclerView= (RecyclerView) view.findViewById(R.id.imgfol_recycle_view);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            rootdir=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        }else
        {
            Toast.makeText(con,"No External Storage!",Toast.LENGTH_SHORT).show();
        }
        File[] file=rootdir.listFiles();
        dirpath=getimgdir(file);
        for(int i=0;i<dirpath.size();i++)
        {
             String path=dirpath.get(i).getPath();
            if(!path.contains("."))
            {
                DirContent dircon = new DirContent(dirpath.get(i).getName(),dirpath.get(i));
                content.add(dircon);
            }
        }
       // RecyclerView.LayoutManager manager=new StaggeredGridLayoutManager()
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        RecycleFolAdpater adpater=new RecycleFolAdpater(content,con);
        recyclerView.setAdapter(adpater);

        return view;
    }


    public ArrayList<File> getimgdir(File[] files)
    {
        ArrayList<File> dlist=new ArrayList<>();
        for(int f=0;f<files.length;f++)
        {
            if(files[f].isDirectory())
            {
                dlist.addAll(getimgdir(files[f].listFiles()));
            }else
            {
                if(files[f].getAbsolutePath().endsWith(".jpg") || files[f].getAbsolutePath().endsWith(".png"))
                {
                    dlist.add(files[f].getAbsoluteFile().getParentFile());
                    f=files.length+1;
                }
            }
        }
        return dlist;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
