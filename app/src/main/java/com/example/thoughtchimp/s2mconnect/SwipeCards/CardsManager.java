package com.example.thoughtchimp.s2mconnect.SwipeCards;

/**
 * Created by thoughtchimp on 11/12/2016.
 */

public class CardsManager {
    static CardsManager instance;
    CardBuilder cardBuilder;
    float childTouchPointX, childTouchPointY;
    boolean fromSwipeCards;

    public static CardsManager getInstance() {
        if (instance == null)
            instance = new CardsManager();

        return instance;
    }

    void setChildTouchPoints(float x, float y) {
        childTouchPointX = x;
        childTouchPointY = y;
    }

    public float getChildTouchPointX() {
        return childTouchPointX;
    }

    public float getChildTouchPointY() {
        return childTouchPointY;
    }

    public boolean isFromSwipeCards() {
        return fromSwipeCards;
    }

    public void setFromSwipeCards(boolean fromSwipeCards) {
        this.fromSwipeCards = fromSwipeCards;
    }
}
