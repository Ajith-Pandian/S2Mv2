package com.example.thoughtchimp.s2mconnect.SwipeCards;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by thoughtchimp on 11/17/2016.
 */

public class CardResizeAnimation extends Animation {
    private final int targetHeight, targetWidth;
    private View view;
    private int startHeight, startWidth;

    public CardResizeAnimation(View view, int startHeight, int targetHeight, int startWidth, int targetWidth) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        this.startHeight = startHeight;
        this.startWidth = startWidth;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        view.getLayoutParams().height = (int) (startHeight + targetHeight * interpolatedTime);
        view.getLayoutParams().width = (int) (startWidth + targetWidth * interpolatedTime);
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}

