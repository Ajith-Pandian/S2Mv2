package com.example.domainlayer.models;

/**
 * Created by thoughtchimp on 12/8/2016.
 */

public  class Bulletin {
    int id, userId;
    String  type, timeStamp;
    BulletinMessage msg;

    public Bulletin(int id, int userId, BulletinMessage msg, String type, String timeStamp) {
        this.id = id;
        this.userId = userId;
        this.msg = msg;
        this.type = type;
        this.timeStamp = timeStamp;
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




}