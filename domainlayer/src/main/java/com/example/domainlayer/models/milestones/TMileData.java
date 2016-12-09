package com.example.domainlayer.models.milestones;

import java.util.ArrayList;

/**
 * Created by thoughtchimp on 12/9/2016.
 */

public class TMileData {
    private int id;
    private String title, body, type;
    private boolean isSingle;
    private ArrayList<String> urlsList;

    public ArrayList<String> getUrlsList() {
        return urlsList;
    }

    public void setUrlsList(ArrayList<String> urlsList) {
        this.urlsList = urlsList;
    }

    public TMileData(int id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public void setSingle(boolean single) {
        isSingle = single;
    }
}
