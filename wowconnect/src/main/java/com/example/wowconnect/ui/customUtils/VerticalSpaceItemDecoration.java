package com.example.wowconnect.ui.customUtils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by thoughtchimp on 1/2/2017.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private final int columsNum;
    private final boolean isTopPadding;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight, int columsNum, boolean isTopPadding) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.columsNum = columsNum;
        this.isTopPadding = isTopPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (isTopPadding && position < columsNum)
            outRect.top = Utils.getInstance().getPixelAsDp(view.getContext(), verticalSpaceHeight * 3);
        outRect.bottom = Utils.getInstance().getPixelAsDp(view.getContext(), verticalSpaceHeight);
    }
}
