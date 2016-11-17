package com.example.thoughtchimp.s2mconnect.SwipeCards;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.thoughtchimp.s2mconnect.R;

import static android.view.View.GONE;

/**
 * Created by thoughtchimp on 11/10/2016.
 */

public class CardBuilder {
    private final int MAX_MARGIN_LEFT = 40, MAX_MARGIN_TOP = 40, MAX_MARGIN_RIGHT = 40, MAX_MARGIN_BOTTOM = 10;
    FrameLayout firstCard, secondCard, thirdCard;
    FrameLayout threeCardsLayout;
    float topCardx, topCardy;
    FrameLayout.LayoutParams topCardLayoutParams;
    int topCardHeight, topCardWidth;
    View removedCard;
    RelativeLayout.LayoutParams layoutParams;

    ;
    float originX, originY;
    private int horizontalDifference = 15, verticalDifference = 20;
    int[] topCardMargin = {MAX_MARGIN_LEFT - horizontalDifference * 2,
            MAX_MARGIN_TOP - verticalDifference * 2,
            MAX_MARGIN_RIGHT - horizontalDifference * 2,
            MAX_MARGIN_BOTTOM + verticalDifference * 2},
            middleCardMargin = {MAX_MARGIN_LEFT - horizontalDifference,
                    MAX_MARGIN_TOP - verticalDifference,
                    MAX_MARGIN_RIGHT - horizontalDifference,
                    MAX_MARGIN_BOTTOM + verticalDifference},
            lastCardMargin = {MAX_MARGIN_LEFT, MAX_MARGIN_TOP, MAX_MARGIN_RIGHT, MAX_MARGIN_BOTTOM};
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

        if (this.layoutParams == null)
            threeCardsLayout.setLayoutParams(layoutParams);
        else
            threeCardsLayout.setLayoutParams(this.layoutParams);
        //Last card
        thirdCard = createCard(false, lastCardMargin, null);
        thirdCard.setId(R.id.last_card_id);
        threeCardsLayout.addView(thirdCard);

        //Middle Card

        secondCard = createCard(false, middleCardMargin, null);
        secondCard.setId(R.id.middle_card_id);
        threeCardsLayout.addView(secondCard);

        //Top card

        firstCard = createCard(false, topCardMargin, null);
        firstCard.setId(R.id.top_card_id);
        topCardLayoutParams = (FrameLayout.LayoutParams) firstCard.getLayoutParams();
        originX = firstCard.getX();
        originY = firstCard.getY();
        threeCardsLayout.addView(firstCard);
        threeCardsLayout.setVisibility(GONE);

        return threeCardsLayout;
    }

    public FrameLayout getFirstCard() {
        return (firstCard);
    }

    public void addContentToFirstCard(View cardLayout) {
        threeCardsLayout.removeView(firstCard);
        firstCard = createCard(true, topCardMargin, cardLayout);
        firstCard.setId(R.id.top_card_id);
        threeCardsLayout.addView(firstCard);
    }

    void addFirstCard() {

        threeCardsLayout.addView(removedCard);
    }

    void addViewInThis(View view) {
        view.setX(originX);
        view.setY(originY);
        threeCardsLayout.addView(view);
    }

    void setLayoutParams(RelativeLayout.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    public void addContentToSecondCard(View cardLayout) {
        removeFirstCard();
        threeCardsLayout.removeView(secondCard);
        secondCard = createCard(true, middleCardMargin, cardLayout);

        secondCard.setId(R.id.middle_card_id);
        threeCardsLayout.addView(secondCard);
        moveSecondCardToTop();
    }

    public void removeFirstCard() {
        threeCardsLayout.removeView(firstCard);
    }

    public View removeAndGetFirstCard() {
        removedCard = firstCard;
        threeCardsLayout.removeView(firstCard);
        return removedCard;
    }

    public void moveSecondCardToTop() {
        //secondCard.animate().translationX(1.2f).scaleY(1.2f);

        secondCard.animate().x(secondCard.getChildAt(0).getX() - horizontalDifference)
                //.scaleYBy(1.5f)
                .y(secondCard.getChildAt(0).getY() - verticalDifference)
                //.alpha(2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //  secondCard.clearAnimation();
                      //  scaleView(secondCard.getChildAt(0));
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
        thirdCard.animate().x(thirdCard.getChildAt(0).getX() - horizontalDifference)
                //.scaleYBy(1.5f)
                .y(thirdCard.getChildAt(0).getY() - verticalDifference);
        // TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE,);
      /*  FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics()));
        lp.setMargins(10, 1, 10, 50);
*/
    }


    void scaleView(View view) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics()));
        lp.setMargins(10, 1, 10, 50);
        view.setLayoutParams(lp);
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
        FrameLayout threeCardsLayout = new FrameLayout(context);

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
            cardView.addView(cardLayout);
        }

        threeCardsLayout.addView(cardView);
        //Card Layout Params
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        // layoutParams.gravity = Gravity.CENTER;
        //cardView.startAnimation(getAnimation(1f, 1f));
        threeCardsLayout.setLayoutParams(layoutParams);

        return threeCardsLayout;
    }
}
