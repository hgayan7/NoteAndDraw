package com.uvstudio.him.noteproject;

import java.io.File;



public class DirContent {
    String path;
    File dirpath;

    public DirContent(String path, File dirpath) {
        this.path = path;
        this.dirpath = dirpath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public File getDirpath() {
        return dirpath;
    }

    public void setDirpath(File dirpath) {
        this.dirpath = dirpath;
    }
}
