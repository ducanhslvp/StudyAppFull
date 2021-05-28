package com.ducanh.appchat.model;

import java.io.Serializable;

public class Subject implements Serializable {
    private String name;
    private String point;


    public Subject() {
    }

    public Subject(String name, String point) {
        this.name = name;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return name;
    }
}
