package com.example.uilayer.customUtils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by thoughtchimp on 1/2/2017.
 */

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;
    private final int columsNum;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight,int columsNum) {
        this.verticalSpaceHeight = verticalSpaceHeight;
        this.columsNum = columsNum;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
       if (position < columsNum)
            outRect.top = Utils.getInstance().getPixelAsDp(view.getContext(), verticalSpaceHeight*3);
        outRect.bottom = Utils.getInstance().getPixelAsDp(view.getContext(), verticalSpaceHeight);
    }
}
