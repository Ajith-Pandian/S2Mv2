package com.example.thoughtchimp.s2mconnect.horizontalScrollCards;

/**
 * Created by thoughtchimp on 11/14/2016.
 */


public class Miles {

    int id, position;
    String url;

    public Miles(int position, int id, String url) {
        this.position = position;
        this.id = id;
        this.url = url;
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
