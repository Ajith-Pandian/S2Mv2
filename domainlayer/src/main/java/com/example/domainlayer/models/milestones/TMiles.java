package com.example.domainlayer.models.milestones;

/**
 * Created by thoughtchimp on 12/9/2016.
 */

public class TMiles {
    private int id, milestoneId,mileIndex,isTraining;
    private String title,note,type;

    public TMiles( int id,int milestoneId, int mileIndex, int isTraining, String title, String note, String type) {
        this.milestoneId = milestoneId;
        this.id = id;
        this.mileIndex = mileIndex;
        this.isTraining = isTraining;
        this.title = title;
        this.note = note;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public int getMileIndex() {
        return mileIndex;
    }

    public void setMileIndex(int mileIndex) {
        this.mileIndex = mileIndex;
    }

    public int getIsTraining() {
        return isTraining;
    }

    public void setIsTraining(int isTraining) {
        this.isTraining = isTraining;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
