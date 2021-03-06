package com.wowconnect.models.milestones;

import com.wowconnect.models.mcq.MCQs;

import java.util.ArrayList;

/**
 * Created by thoughtchimp on 12/9/2016.
 */

public class TMiles {
    private int id, milestoneId, mileIndex;
    private String title, note, type;
    private ArrayList<TMileData> mileData;
    private ArrayList<MCQs> mcqs;
    private boolean isCompletable,canComplete;

    public TMiles() {
    }

    public TMiles(int id, String title, String note, String type, int mileIndex, boolean isCompletable) {
        this.id = id;
        this.mileIndex = mileIndex;
        this.title = title;
        this.note = note;
        this.type = type;
        this.isCompletable = isCompletable;
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

    public ArrayList<TMileData> getMileData() {
        return mileData;
    }

    public void setMileData(ArrayList<TMileData> mileData) {
        this.mileData = mileData;
    }

    public boolean isCompletable() {
        return isCompletable;
    }

    public void setCompletable(boolean completable) {
        isCompletable = completable;
    }

    public ArrayList<MCQs> getMcqs() {
        return mcqs;
    }

    public void setMcqs(ArrayList<MCQs> mcqs) {
        this.mcqs = mcqs;
    }

    public boolean canComplete() {
        return canComplete;
    }

    public void setCanComplete(boolean canComplete) {
        this.canComplete = canComplete;
    }
}
