package com.uvstudio.him.noteproject;

import java.io.File;



public class ImageContent {

    File imgpath;

    public ImageContent(File imgpath) {
        this.imgpath = imgpath;
    }

    public File getImgpath() {
        return imgpath;
    }

    public void setImgpath(File imgpath) {
        this.imgpath = imgpath;
    }
}
