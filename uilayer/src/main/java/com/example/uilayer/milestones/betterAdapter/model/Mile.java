package com.example.uilayer.milestones.betterAdapter.model;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class Mile implements Milestones {
    private int id, position;
    private String title, content;

    public Mile(int id, int position, String title, String content) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public int getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }
    @Override
    public int getType()
    {
        return 1;
    }
}
