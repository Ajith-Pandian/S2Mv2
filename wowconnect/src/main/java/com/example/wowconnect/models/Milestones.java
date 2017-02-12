package com.example.wowconnect.models;

/**
 * Created by thoughtchimp on 12/28/2016.
 */

public class Milestones {
    int id;
    String name;

    public Milestones(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
