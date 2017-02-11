package com.example.domainlayer.models;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.CustomDao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import static com.example.domainlayer.Constants.TABLE_NAME_SCHOOL_ACTIVITIES;

/**
 * Created by thoughtchimp on 12/8/2016.
 */
@DatabaseTable(tableName = TABLE_NAME_SCHOOL_ACTIVITIES, daoClass = CustomDao.class)

public class SclActs {

    @DatabaseField(columnName = Constants.KEY_ID, id = true)
    private int id;

    @DatabaseField(columnName = Constants.KEY_LIKES_COUNT)
    private int likesCount;

    @DatabaseField(columnName = Constants.KEY_MESSAGE)
    private String msg;

    @DatabaseField(columnName = Constants.KEY_ICON)
    private String icon;

    @DatabaseField(columnName = Constants.KEY_TYPE)
    private String type;

    @DatabaseField(columnName = Constants.KEY_TITLE)
    private String title;

    @DatabaseField(columnName = Constants.KEY_TIMESTAMP)
    private String timeStamp;

    @DatabaseField(columnName = Constants.IS_LIKED)
    private boolean isLiked;

    public SclActs(int id, String type, String title, String msg, String timeStamp, int likesCount, boolean isLiked, String icon) {
        this.id = id;
        this.title = title;
        this.msg = msg;
        this.type = type;
        this.timeStamp = timeStamp;
        this.likesCount = likesCount;
        this.isLiked = isLiked;
        this.icon = icon;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}