package com.wowconnect.models.milestones;

import java.util.ArrayList;

/**
 * Created by thoughtchimp on 12/9/2016.
 */

public class TMileData {
    private int id;
    private String title, body, type;
    private boolean isSingle;
    private ArrayList<String> urlsList, imagesList, hdImagesList;
    private ArrayList<String> videoIds;


    public ArrayList<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }

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
    public TMileData(String title, String type) {
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

    public ArrayList<String> getHdImagesList() {
        return hdImagesList;
    }

    public void setHdImagesList(ArrayList<String> hdImagesList) {
        this.hdImagesList = hdImagesList;
    }

    public ArrayList<String> getVideoIds() {
        return videoIds;
    }

    public void setVideoIds(ArrayList<String> videoIds) {
        this.videoIds = videoIds;
    }
}
