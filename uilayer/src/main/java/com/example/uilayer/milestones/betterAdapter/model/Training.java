package com.example.uilayer.milestones.betterAdapter.model;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class Training implements Milestones{
    private int id,position;
    private String title, content;

    public Training(int id, int position, String title, String content) {
        this.id = id;
        this.position = position;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override

    public int getType()
    {
        return 2;
    }
}
