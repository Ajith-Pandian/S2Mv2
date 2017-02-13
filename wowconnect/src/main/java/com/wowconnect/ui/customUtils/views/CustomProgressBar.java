package com.wowconnect.ui.customUtils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.wowconnect.R;


/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class CustomProgressBar extends View {
    // % value of the progressbar.
    int progressBarValue = 0;

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        progressBarValue = a.getInt(R.styleable.CustomProgressBar_progressBarValue, progressBarValue);
        a.recycle();
        //progressBarValue = attrs.getAttributeIntValue(null, "progressBarValue", 0);
    }

    RectF backgroundRect;
    Paint backgroundPaint;
    Paint barPaint;
    RectF barRect;

    void init(Canvas canvas) {
        backgroundRect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        //background paint
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setAntiAlias(true);
        //progress bar paint
        barPaint = new Paint();
        barPaint.setColor(getContext().getResources().getColor(R.color.green4));
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setAntiAlias(true);
        float progress = (backgroundRect.width() / 100) * progressBarValue;
        barRect = new RectF(0, 0, progress, canvas.getClipBounds().bottom);
    }

    public void setProgress(int value) {
        progressBarValue = value;
        invalidate();
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
        float cornerRadius = 30.0f;
        //Draw background
        canvas.drawRect(backgroundRect, backgroundPaint);

        // Draw the progress bar.
        canvas.drawRect(barRect, barPaint);

       /* // Draw progress text in the middle.
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(34);

        String text = progressBarValue + "%";
        Rect textBounds = new Rect();

        textPaint.getTextBounds(text, 0, text.length(), textBounds);

        canvas.drawText(text,
                backgroundRect.centerX() - (textBounds.width() / 2),
                backgroundRect.centerY() + (textBounds.height() / 2),
                textPaint);*/

    }
}