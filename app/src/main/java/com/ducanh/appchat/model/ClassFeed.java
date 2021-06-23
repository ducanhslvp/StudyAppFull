package com.ducanh.appchat.model;

public class ClassFeed {
    private String userID;
    private String content;
    private String type;

    public ClassFeed(String userID, String content, String type) {
        this.userID = userID;
        this.content = content;
        this.type = type;
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
}
