package com.example.wowconnect.ui.network;

/**
 * Created by thoughtchimp on 12/21/2016.
 */

class ProfileModel {
    int iconId;
    String primaryText, secondaryText;

    public ProfileModel(int iconId, String primaryText, String secondaryText) {
        this.iconId = iconId;
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }
}