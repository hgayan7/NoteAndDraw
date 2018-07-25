package com.uvstudio.him.noteproject;


public class Note {
    public Note(int id) {
        this.id = id;
    }

    int id;

    public Note(String title, String info, String priority) {
        this.title = title;
        this.info = info;
        this.priority = priority;
    }

    String title;
    String info;
    String priority;
    public Note(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }




    public Note(int id, String title, String info, String priority) {
        this.id = id;
        this.title = title;
        this.info = info;
        this.priority = priority;
    }
}


