package com.example.wowconnect.models.mcq;

import java.io.Serializable;

/**
 * Created by thoughtchimp on 12/15/2016.
 */

public class McqOptions implements Serializable {
    private String text;
    private boolean isSelected, isRight, isWrong, isNotSelected;

    public boolean isNotSelected() {
        return isNotSelected;
    }

    public void setNotSelected(boolean notSelected) {
        isNotSelected = notSelected;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public boolean isWrong() {
        return isWrong;
    }

    public void setWrong(boolean wrong) {
        isWrong = wrong;
    }
}
