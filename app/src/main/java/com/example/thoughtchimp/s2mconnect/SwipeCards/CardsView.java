package com.example.thoughtchimp.s2mconnect.SwipeCards;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.thoughtchimp.s2mconnect.R;

/**
 * Created by thoughtchimp on 11/12/2016.
 */

public class CardsView extends RelativeLayout {
    int NO_OF_CARDS = 4;
    float HORIZONTAL_DIFFERENCE = 15, VERTICAL_DIFFERENCE = 20;
    int[][] cardsMargins;
    Context context;
    float density;
    private int MAX_MARGIN_LEFT = 40, MAX_MARGIN_TOP = 40, MAX_MARGIN_RIGHT = 40, MAX_MARGIN_BOTTOM = 10;

    CardsView(Context context) {
        super(context);
        this.context = context;
    }

    public CardsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CardsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeCardsView, defStyle, 0);
        NO_OF_CARDS = a.getInt(R.styleable.SwipeCardsView_numCards, NO_OF_CARDS);
        HORIZONTAL_DIFFERENCE = a.getFloat(R.styleable.SwipeCardsView_horizontalDifference, HORIZONTAL_DIFFERENCE);
        VERTICAL_DIFFERENCE = a.getFloat(R.styleable.SwipeCardsView_verticalDifference, VERTICAL_DIFFERENCE);
        a.recycle();


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        calculateMargins();

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        FrameLayout layout;
        for (int i = 0; i < NO_OF_CARDS; i++) {
            if (i == NO_OF_CARDS - 1) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                View cardsContentView = inflater.inflate(R.layout.test, null);
                layout = createCard(true, cardsMargins[i], cardsContentView);
            } else
                layout = createCard(false, cardsMargins[i], null);
            addViewInLayout(layout, i, layoutParams);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ViewGroup.MarginLayoutParams margins = ViewGroup.MarginLayoutParams.class.cast(getLayoutParams());
        MAX_MARGIN_LEFT = MAX_MARGIN_LEFT + margins.leftMargin;
        MAX_MARGIN_TOP = 40 + margins.topMargin;
        MAX_MARGIN_RIGHT = 40 + margins.rightMargin;
        MAX_MARGIN_BOTTOM = 10 + margins.bottomMargin;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    int getMarginInDp(int margin) {
        if (density == 0f)
            density = getContext().getResources().getDisplayMetrics().density;

        return (int) (margin / density);
    }

    void calculateMargins() {
        cardsMargins = new int[NO_OF_CARDS][];
        for (int i = 0; i < NO_OF_CARDS; i++) {
            cardsMargins[i] = getMargins(i);
        }
        for (int i = 0; i < NO_OF_CARDS; i++) {
            for (int j = 0; j < 4; j++) {
                Log.d("Margins", "calculateMargins: " + i + " " + j + " " + cardsMargins[i][j]);
            }

        }
    }

    int[] getMargins(int cardIndex) {
        int[] margins = {(int) (MAX_MARGIN_LEFT - (HORIZONTAL_DIFFERENCE * cardIndex)),
                (int) (MAX_MARGIN_TOP - (VERTICAL_DIFFERENCE * cardIndex)),
                (int) (MAX_MARGIN_RIGHT - (HORIZONTAL_DIFFERENCE * cardIndex)),
                (int) (MAX_MARGIN_BOTTOM + (VERTICAL_DIFFERENCE * cardIndex))};
        return margins;
    }

    private FrameLayout createCard(boolean isTopCard, int[] margins, View cardLayout) {
        FrameLayout threeCardsLayout = new FrameLayout(context);

        CardView cardView = new CardView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics())
        );
        params.setMargins(margins[0], margins[1], margins[2], margins[3]);

        cardView.setLayoutParams(params);
        cardView.setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()));
        if (isTopCard) {
            cardView.addView(cardLayout);
        }
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (2*scale + 0.5f);
        threeCardsLayout.setPadding(10,0,10,0);
        threeCardsLayout.addView(cardView);

        return threeCardsLayout;
    }

}
