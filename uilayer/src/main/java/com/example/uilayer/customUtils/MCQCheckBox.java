package com.example.uilayer.customUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.example.uilayer.R;

/**
 * Created by thoughtchimp on 12/8/2016.
 */

public class MCQCheckBox extends ImageButton {

    private static final int[] STATE_SELECTED = {R.attr.state_selected};
    private static final int[] STATE_NOT_SELECTED = {R.attr.state_not_selected};
    private static final int[] STATE_RIGHT = {R.attr.state_right};
    private static final int[] STATE_WRONG = {R.attr.state_wrong};

    private boolean isSelected = false;
    private boolean isRight = false;
    private boolean isWrong = false;
    private boolean isNotSelected = false;
    public MCQCheckBox(Context context) {
        super(context);
    }

    public MCQCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 4);
        if (isSelected) {
            mergeDrawableStates(drawableState, STATE_SELECTED);
        }
       else if (isNotSelected) {
            mergeDrawableStates(drawableState, STATE_NOT_SELECTED);
        }
       else if (isRight) {
            mergeDrawableStates(drawableState, STATE_RIGHT);
        }
       else if (isWrong) {
            mergeDrawableStates(drawableState, STATE_WRONG);
        }
        return drawableState;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    public void setWrong(boolean wrong) {
        isWrong = wrong;
    }

    public void setNotSelected(boolean notSelected) {
        isNotSelected = notSelected;
    }
}
