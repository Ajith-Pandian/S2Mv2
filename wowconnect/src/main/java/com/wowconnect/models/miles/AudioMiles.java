package com.wowconnect.models.miles;

import java.io.Serializable;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class AudioMiles implements Serializable {
    int id, position;
    String image_url,content_url;

    public AudioMiles(int position, int id, String image_url,String content_url) {
        this.position = position;
        this.id = id;
        this.image_url = image_url;
        this.content_url=content_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

}
