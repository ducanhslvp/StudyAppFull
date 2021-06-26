package com.ducanh.appchat.model;

public class ClassFeed {
    private String userID;
    private String content;
    private String type;
    private String date;

    public ClassFeed(String userID, String content, String type, String date) {
        this.userID = userID;
        this.content = content;
        this.type = type;
        this.date = date;
    }

    public ClassFeed() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
