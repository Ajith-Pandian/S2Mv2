package com.wowconnect.ui.customUtils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by thoughtchimp on 11/14/2016.
 */

public class VideoMilesDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpace;
    private Context context;

    public VideoMilesDecoration(Context context, int horizontalSpace) {
        this.context = context;
        this.horizontalSpace  = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                horizontalSpace, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = horizontalSpace;
        if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
            outRect.right = horizontalSpace;
        }
    }
}