package com.ducanh.appchat.model;

import java.io.Serializable;

public class Test implements Serializable {
    private String name;
    private String subjectName;
    private Question question;

    public Test() {
    }

    public Test(String name, String subjectName, Question question) {
        this.name = name;
        this.subjectName = subjectName;
        this.question = question;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Test{" +
                "name='" + name + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", question=" + question +
                '}';
    }
}
