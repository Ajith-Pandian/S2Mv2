package com.example.uilayer.customUtils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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

        // if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
       int position = parent.getChildAdapterPosition(view) + 1;
       if (position % columsNum == 0)
     outRect.right = getPixelAsDp(horizontalSpace);
      if(position<=(parent.getAdapter().getItemCount()-2))
        outRect.bottom = getPixelAsDp(verticalSpace);


    }

    int getPixelAsDp(int pixel) {
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, context.getResources().getDisplayMetrics());
        return dp;
    }
}