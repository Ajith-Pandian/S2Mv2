package com.example.uilayer.customUtils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;

/**
 * Created by thoughtchimp on 11/26/2016.
 */

public class HeightWrapListView extends ListView {

    android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;
    private boolean hasExtraSpace = false;
    int space;

    public HeightWrapListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HeightWrapListView);
        hasExtraSpace = a.getBoolean(R.styleable.HeightWrapListView_hasExtraSpace, false);
        a.recycle();
       //hasExtraSpace = attrs.getAttributeBooleanValue(null, "hasExtraSpace", false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != oldCount) {
            int height = getChildAt(0).getHeight();// + 1 ;
            oldCount = getCount();
            params = getLayoutParams();
            if (hasExtraSpace)
                space = 10;
            else
                space = 1;
            params.height = getCount() * (height + Utils.getInstance().getPixelAsDp(getContext(), space));
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }

}