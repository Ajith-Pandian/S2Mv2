package com.example.uilayer.models;

/**
 * Created by thoughtchimp on 11/9/2016.
 */

public class SchoolDetails {
    private String name,message,date,time,likes;

    public SchoolDetails(String name, String message, String date, String time, String likes) {
    this.name=name;
    this.message=message;
    this.date=date;
    this.time=time;
    this.likes=likes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
