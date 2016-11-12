package com.example.thoughtchimp.s2mconnect;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

/**
 * Created by thoughtchimp on 11/10/2016.
 */

public class CardBuilder {
    private final int MAX_MARGIN_LEFT = 40, MAX_MARGIN_TOP = 40, MAX_MARGIN_RIGHT = 40, MAX_MARGIN_BOTTOM = 10;
    FrameLayout firstCard, secondCard, thirdCard;
    FrameLayout threeCardsLayout;
    private int horizontalDifference = 15, verticalDifference = 20;
    private Context context;

    public CardBuilder(Context context) {
        this.context = context;
    }
    public CardBuilder(Context context, int verticalDifference, int horizontalDifference) {
        this.context = context;
        this.horizontalDifference = horizontalDifference;
        this.verticalDifference = verticalDifference;
    }

    public ViewGroup makeAndGetCards() {
        //Parent Layout
        threeCardsLayout = new FrameLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT

                //(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics())
        );
        layoutParams.setMargins(0, 0, 0, 0);
        threeCardsLayout.setLayoutParams(layoutParams);

        //Last card
        int[] margins3 = {MAX_MARGIN_LEFT, MAX_MARGIN_TOP, MAX_MARGIN_RIGHT, MAX_MARGIN_BOTTOM};
        thirdCard = createCard(false, margins3,null);
        thirdCard.setId(R.id.last_card_id);
        threeCardsLayout.addView(thirdCard);

        //Middle Card
        int[] margins2 = {MAX_MARGIN_LEFT - horizontalDifference,
                MAX_MARGIN_TOP - verticalDifference,
                MAX_MARGIN_RIGHT - horizontalDifference,
                MAX_MARGIN_BOTTOM + verticalDifference};
        secondCard = createCard(false, margins2,null);
        secondCard.setId(R.id.middle_card_id);
        threeCardsLayout.addView(secondCard);

        //Top card
        int[] margins = {MAX_MARGIN_LEFT - horizontalDifference * 2,
                MAX_MARGIN_TOP - verticalDifference * 2,
                MAX_MARGIN_RIGHT - horizontalDifference * 2,
                MAX_MARGIN_BOTTOM + verticalDifference * 2};
        firstCard = createCard(false, margins,null);
        firstCard.setId(R.id.top_card_id);
        threeCardsLayout.addView(firstCard);


        return threeCardsLayout;
    }

    public FrameLayout getFirstCard() {
        return firstCard;
    }

   public void addContentToFirstCard(View cardLayout)
    {threeCardsLayout.removeView(firstCard);
        int[] margins = {MAX_MARGIN_LEFT - horizontalDifference * 2,
                MAX_MARGIN_TOP - verticalDifference * 2,
                MAX_MARGIN_RIGHT - horizontalDifference * 2,
                MAX_MARGIN_BOTTOM + verticalDifference * 2};
        firstCard = createCard(true, margins,cardLayout);
        firstCard.setId(R.id.top_card_id);
        threeCardsLayout.addView(firstCard);
    }


    public FrameLayout getSecondCard() {
        return secondCard;
    }

    public FrameLayout getThirdCard() {
        return thirdCard;
    }

    private Animation getAnimation(float xSize, float ySize) {
        Animation animation = new ScaleAnimation(0f, xSize, 0f, ySize, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        return animation;
    }

    private FrameLayout createCard(boolean isTopCard, int[] margins, View cardLayout) {
        FrameLayout linearLayout = new FrameLayout(context);
        CardView cardView = new CardView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics())
        );
        params.setMargins(margins[0], margins[1], margins[2], margins[3]);
        //params.addRule(FrameLayout.ALIGN_PARENT_BOTTOM, FrameLayout.TRUE);

        cardView.setLayoutParams(params);
        cardView.setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()));
        if (isTopCard) {
            //Card Items layout
            // LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            // View childLayout = inflater.inflate(R.layout.card_school_update,
            //        null);
            cardView.addView(cardLayout);
        }
        //Card Layout Params
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        layoutParams.gravity = Gravity.CENTER;
        //layoutParams.addRule(FrameLayout.CENTER_IN_PARENT, FrameLayout.TRUE);
        //cardView.startAnimation(getAnimation(1f, 1f));
        linearLayout.addView(cardView);

        return linearLayout;
    }
}
