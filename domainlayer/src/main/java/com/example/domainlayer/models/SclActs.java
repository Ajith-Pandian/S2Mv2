package com.example.domainlayer.models;

/**
 * Created by thoughtchimp on 12/8/2016.
 */

public  class SclActs {
    int id, userId;
    String  type, timeStamp;
    String msg;

    public SclActs(int id, int userId, String msg, String type, String timeStamp) {
        this.id = id;
        this.userId = userId;
        this.msg = msg;
        this.type = type;
        this.timeStamp = timeStamp;
    }

    public  SclActs() {

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




}