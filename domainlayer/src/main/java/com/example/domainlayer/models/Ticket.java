package com.example.domainlayer.models;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by thoughtchimp on 1/2/2017.
 */

public class Ticket {
    private String id;
    private Map<String,String> userIds;
    private String date, category, subject, status, createdAt, updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

 /*   public Map<String,String> getUserIds() {
        return userIds;
    }

    public void setUserIds(Map<String,String> userIds) {
        this.userIds = userIds;
    }*/

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
