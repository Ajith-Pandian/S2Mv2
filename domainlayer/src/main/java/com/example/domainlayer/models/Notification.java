package com.example.domainlayer.models;

/**
 * Created by thoughtchimp on 1/20/2017.
 */

public class Notification {
    private String title, content,date,type;

    public Notification(String title, String content, String date, String type) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
