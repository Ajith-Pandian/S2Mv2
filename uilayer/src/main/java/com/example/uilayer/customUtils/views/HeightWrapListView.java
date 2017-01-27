package com.example.uilayer.customUtils.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

import com.example.uilayer.customUtils.Utils;

/**
 * Created by thoughtchimp on 11/26/2016.
 */

public class HeightWrapListView extends ListView {

    android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public HeightWrapListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != oldCount)
        {
            int height = getChildAt(0).getHeight();// + 1 ;
            oldCount = getCount();
            params = getLayoutParams();
            params.height = getCount() * (height+ Utils.getInstance().getPixelAsDp(getContext(),10));
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }

}