package com.ducanh.appchat.model;

public class Noti {
    private int id;
    private String time;
    private String title;

    public Noti(int id, String time, String title) {
        this.id = id;
        this.time = time;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
