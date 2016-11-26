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
    private final int verticalSpace;
    private final int columsNum;
    private Context context;

    public HorizontalSpaceItemDecoration(Context context, int horizontalSpace, int verticalSpace, int columsNum) {
        this.horizontalSpace = horizontalSpace;
        this.verticalSpace = verticalSpace;
        this.columsNum = columsNum;
        this.context = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view) + 1;
        if (position % columsNum == 0)
            outRect.right = Utils.getInstance().getPixelAsDp(context, horizontalSpace);
        if (position <= (parent.getAdapter().getItemCount() - 2))
            outRect.bottom = Utils.getInstance().getPixelAsDp(context, verticalSpace);
    }
}