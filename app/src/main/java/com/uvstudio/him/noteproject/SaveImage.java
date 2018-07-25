package com.uvstudio.him.noteproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class SaveImage {
    MediaScannerConnection conn;
    File file,dir;
    Context con;

    public SaveImage(Context con) {
        this.con = con;
    }

    public void savetosdcard(Bitmap bitmap, String filename)
    {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {

            dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/NoteDraw");
            if(!dir.exists())
            {
                dir.mkdir();
            }
            file=new File(dir,filename);
            try
            {
                FileOutputStream fo=new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,100,fo);
                fo.flush();
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void startScan()
    {
        if(conn!=null) conn.disconnect();
        conn = new MediaScannerConnection(con, new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                try {
                    conn.scanFile(file.getAbsolutePath(), "image/*");
                }catch(IllegalStateException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                conn.disconnect();
            }
        });
        conn.connect();
    }

    public ArrayList<File> imageReader(File root) {
        ArrayList<File> f=new ArrayList<>();
        File[] file=root.listFiles();

        for(int i=0;i<file.length;i++){
            if(file[i].isDirectory()){
                f.addAll( imageReader(file[i]));
            }else{
                if(file[i].getName().endsWith(".jpg") ){
                    f.add(file[i]);
                }else if(file[i].getName().endsWith(".png")){
                    f.add(file[i]);
                }
            }
        }
        return f;
    }

}
