package com.wowconnect.models;

/**
 * Created by thoughtchimp on 1/20/2017.
 */

public class Notification {
    private String title, data, date, type;
    private int id;
    private boolean hasRead;

    public Notification(int id, String title, String data, String date, String type) {
        this.id = id;
        this.title = title;
        this.data = data;
        this.date = date;
        this.type = type;
    }

    public Notification() {
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public boolean hasRead() {
        return hasRead;
    }

    public void setRead(boolean hasRead) {
        this.hasRead = hasRead;
    }
}
