package com.example.uilayer.customUtils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by thoughtchimp on 11/14/2016.
 */

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpace;
    private Context context;

    public HorizontalSpaceItemDecoration(Context context, int horizontalSpace) {
        this.horizontalSpace = horizontalSpace;
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {

        // if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
        // outRect.right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalSpace, context.getResources().getDisplayMetrics());

    }
}