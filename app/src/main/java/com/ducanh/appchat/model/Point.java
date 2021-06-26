package com.ducanh.appchat.model;

public class Point {
    private String id;
    private String point;
    private String subject;
    private String time;

    public Point(String id, String point, String subject, String time) {
        this.id = id;
        this.point = point;
        this.subject = subject;
        this.time = time;
    }

    public Point() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
