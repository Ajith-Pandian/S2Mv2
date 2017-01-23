package com.example.uilayer.customUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

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
            params.height = getCount() * (height+Utils.getInstance().getPixelAsDp(getContext(),1));
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }

}