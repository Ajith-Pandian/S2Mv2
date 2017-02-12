package com.example.wowconnect.ui.customUtils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.example.wowconnect.R;

/**
 * Created by thoughtchimp on 12/8/2016.
 */

public class MCQCheckBox extends ImageButton {
    
    private static final int[] STATE_SELECTED = {R.attr.state_is_selected};
    private static final int[] STATE_NOT_SELECTED = {R.attr.state_not_selected};
    private static final int[] STATE_RIGHT = {R.attr.state_right};
    private static final int[] STATE_WRONG = {R.attr.state_wrong};

    private boolean isSelected = false;
    private boolean isRight = false;
    private boolean isWrong = false;
    private boolean isNotSelected = true;

    public MCQCheckBox(Context context) {
        super(context);
    }

    public MCQCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.answer_check, 0, 0);
        try {
            isSelected = a.getBoolean(R.styleable.answer_check_state_is_selected, false);
            isNotSelected = a.getBoolean(R.styleable.answer_check_state_not_selected, true);
            isWrong = a.getBoolean(R.styleable.answer_check_state_wrong, false);
            isRight = a.getBoolean(R.styleable.answer_check_state_right, false);
        } finally {
            a.recycle();
        }

    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 4);
        if (isSelected) {
            mergeDrawableStates(drawableState, STATE_SELECTED);
        } else if (isRight) {
            mergeDrawableStates(drawableState, STATE_RIGHT);
        } else if (isWrong) {
            mergeDrawableStates(drawableState, STATE_WRONG);
        } else if (isNotSelected) {
            mergeDrawableStates(drawableState, STATE_NOT_SELECTED);
        }
        return drawableState;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
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

    public boolean isNotSelected() {
        return isNotSelected;
    }

    public void setNotSelected(boolean notSelected) {
        isNotSelected = notSelected;
    }
}
