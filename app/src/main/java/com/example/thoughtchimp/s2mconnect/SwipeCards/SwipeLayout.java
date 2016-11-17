package com.example.thoughtchimp.s2mconnect.SwipeCards;

import android.animation.Animator;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Adapter;
import android.widget.RelativeLayout;

/**
 * Created by thoughtchimp on 11/12/2016.
 */

public class SwipeLayout extends RelativeLayout {
    int currentCardPosition;
    RelativeLayout.LayoutParams cardsLayoutParams;
    CardBuilder cardBuilder;
    ViewGroup cards;
    View view;
    int top, left;
    float objectX, objectY;
    int first = 1;
    float startedX, startedY;
    boolean isRemoved = false;
    boolean fromSwipeCards;
    private CardsView swipeCardsView;
    private Adapter adapter;
    private final DataSetObserver observer = new DataSetObserver() {

        @Override
        public void onChanged() {
            refreshViewsFromAdapter();
        }

        @Override
        public void onInvalidated() {
            removeAllViews();
        }
    };
    private View currentCard;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        cardBuilder = new CardBuilder(context);

    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(observer);
        }
        this.adapter = adapter;
        if (this.adapter != null) {
            this.adapter.registerDataSetObserver(observer);
        }
        initViewsFromAdapter();
    }

    protected void initViewsFromAdapter() {
        // removeAllViews();
        if (adapter != null) {
            // for (int i = 0; i < adapter.getCount(); i++) {
            // addView(adapter.getView(0, null, this), 0);
            // }

            cardBuilder.setLayoutParams(cardsLayoutParams);
            cards = cardBuilder.makeAndGetCards();
            addView(cards);
            addCard();
        }
    }

    void addCard() {
        cardBuilder.addContentToFirstCard(adapter.getView(0, null, this));
    }

    void addNextCard() {
        int nextPosition = currentCardPosition + 1;
        if (nextPosition < adapter.getCount()) {
            cardBuilder.addContentToFirstCard(adapter.getView(nextPosition, null, this));
            currentCardPosition = nextPosition;
        } else {
            cardBuilder.addContentToSecondCard(adapter.getView(0, null, this));
        }
    }

    protected void refreshViewsFromAdapter() {
        int childCount = getChildCount();
        int adapterSize = adapter.getCount();
        int reuseCount = Math.min(childCount, adapterSize);

        for (int i = 0; i < reuseCount; i++) {
            adapter.getView(i, getChildAt(i), this);
        }

        if (childCount < adapterSize) {
            for (int i = childCount; i < adapterSize; i++) {
                addView(adapter.getView(i, null, this), i);
            }
        } else if (childCount > adapterSize) {
            removeViews(adapterSize, childCount);
        }
    }

    void setSwipeCardsView(CardsView swipeCardsView) {
        this.swipeCardsView = swipeCardsView;
        this.cardsLayoutParams = (RelativeLayout.LayoutParams) swipeCardsView.getLayoutParams();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (fromSwipeCards=CardsManager.getInstance().isFromSwipeCards()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view = cardBuilder.removeAndGetFirstCard();
                    startedX = event.getX();
                    startedY = event.getY();
                    ViewCompat.setElevation(view, 24);
                    objectX=CardsManager.getInstance().getChildTouchPointX();
                    objectY=CardsManager.getInstance().getChildTouchPointY();
                    view.setX(startedX - objectX);
                    view.setY(startedY - objectY);
                    addView(view);

                    break;

                case MotionEvent.ACTION_CANCEL:
                    Log.d("TouchView", "On Touch cancelled.");

                    break;
                case MotionEvent.ACTION_UP:
                    view.animate().x(startedX - objectX)
                            .y(startedY - objectY)
                            .setInterpolator(new OvershootInterpolator()).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            fromSwipeCards = false;
                            CardsManager.getInstance().setFromSwipeCards(fromSwipeCards);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            removeView(view);
                            cardBuilder.addViewInThis(view);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    break;
                case MotionEvent.ACTION_MOVE:
                    view.setX(event.getX() - objectX);
                    view.setY(event.getY() - objectY);
                    break;
            }
        }
        invalidate();

        return true;
    }
}
