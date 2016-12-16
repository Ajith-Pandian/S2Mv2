package com.example.uilayer.models;

/**
 * Created by thoughtchimp on 12/15/2016.
 */

public class McqOptions {
    String label, text;
    boolean isSelected;
    boolean isRight;
    boolean isWrong;

    public boolean isNotSelected() {
        return isNotSelected;
    }

    public void setNotSelected(boolean notSelected) {
        isNotSelected = notSelected;
    }

    boolean isNotSelected;

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
