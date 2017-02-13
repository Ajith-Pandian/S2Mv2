package com.wowconnect.models;

/**
 * Created by thoughtchimp on 12/8/2016.
 */

public  class Bulletin {
    int id, userId;
    String  type, timeStamp;
    BulletinMessage msg;
    int isLiked;

    public Bulletin(int id, int userId, BulletinMessage msg, String type, String timeStampm, int isLiked) {
        this.id = id;
        this.userId = userId;
        this.msg = msg;
        this.type = type;
        this.timeStamp = timeStamp;
        this.isLiked=isLiked;
    }

    public Bulletin() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BulletinMessage getMsg() {
        return msg;
    }

    public void setMsg(BulletinMessage msg) {
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

    public int getLiked() {
        return isLiked;
    }

    public void setLiked(int liked) {
        isLiked = liked;
    }
}