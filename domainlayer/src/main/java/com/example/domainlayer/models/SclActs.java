package com.example.domainlayer.models;

/**
 * Created by thoughtchimp on 12/8/2016.
 */

public class SclActs {
    private int id, likesCount;
    private String type, timeStamp, msg;
    private boolean isLiked;

    public SclActs(int id, String type, String msg, String timeStamp, int likesCount, boolean isLiked) {
        this.id = id;
        this.msg = msg;
        this.type = type;
        this.timeStamp = timeStamp;
        this.likesCount = likesCount;
        this.isLiked = isLiked;
    }

    public SclActs() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}