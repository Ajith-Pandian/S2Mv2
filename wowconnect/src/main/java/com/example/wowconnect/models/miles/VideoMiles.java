package com.example.wowconnect.models.miles;

import java.io.Serializable;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class VideoMiles implements Serializable {
    int  position;
    String videoId,imageUrl,hdImageUrl;

    public VideoMiles(int position, String videoId, String imageUrl, String hdImageUrl) {
        this.position = position;
        this.videoId = videoId;
        this.imageUrl = imageUrl;
        this.hdImageUrl = hdImageUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHdImageUrl() {
        return hdImageUrl;
    }

    public void setHdImageUrl(String hdImageUrl) {
        this.hdImageUrl = hdImageUrl;
    }
}
