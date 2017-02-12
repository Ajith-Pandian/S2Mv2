package com.example.wowconnect.models;

/**
 * Created by thoughtchimp on 12/8/2016.
 */

public  class BulletinMessage
{
    String title,image,body;

    public BulletinMessage(String title, String image, String body) {
        this.title = title;
        this.image = image;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
