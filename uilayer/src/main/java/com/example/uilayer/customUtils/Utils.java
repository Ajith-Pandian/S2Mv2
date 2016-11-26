package com.example.uilayer.customUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.TypedValue;

/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class Utils {
    private static Utils mInstance;

    public static Utils getInstance() {
        if (mInstance == null)
            mInstance = new Utils();
        return mInstance;
    }

    public RoundedBitmapDrawable getCirclularImage(Context context, Bitmap imageBitmap) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), imageBitmap);
        roundedBitmapDrawable.setCircular(true);
        return roundedBitmapDrawable;
    }
   public int getPixelAsDp(Context context,int pixel) {
        return  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pixel, context.getResources().getDisplayMetrics());
    }
}
