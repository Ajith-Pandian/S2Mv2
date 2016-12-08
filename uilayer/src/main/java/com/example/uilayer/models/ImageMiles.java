package com.example.uilayer.models;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class ImageMiles {
    private int id, position;
    private String url;
    private String title;

    public ImageMiles(int position, int id, String title, String url) {
        this.position = position;
        this.id = id;
        this.url = url;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}