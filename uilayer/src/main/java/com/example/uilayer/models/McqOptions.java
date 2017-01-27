package com.example.uilayer.models;

import java.io.Serializable;

/**
 * Created by thoughtchimp on 12/15/2016.
 */

public class McqOptions implements Serializable {
    private String label, text;
    private boolean isSelected, isRight, isWrong, isNotSelected;

    public boolean isNotSelected() {
        return isNotSelected;
    }

    public void setNotSelected(boolean notSelected) {
        isNotSelected = notSelected;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
