package com.example.thoughtchimp.s2mconnect.SwipeCards;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;

import com.example.thoughtchimp.s2mconnect.CardBuilder;

/**
 * Created by thoughtchimp on 11/12/2016.
 */

public class SwipeLayout extends RelativeLayout {
    int currentCardPosition;
    RelativeLayout.LayoutParams cardsLayoutParams;
    CardBuilder cardBuilder;
    ViewGroup cards;
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

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        cardBuilder = new CardBuilder(context);
        cards = cardBuilder.makeAndGetCards();
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
}
