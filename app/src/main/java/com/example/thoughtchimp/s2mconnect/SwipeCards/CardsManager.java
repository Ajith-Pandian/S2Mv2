package com.example.thoughtchimp.s2mconnect.SwipeCards;

import android.content.Context;
import android.view.View;

/**
 * Created by thoughtchimp on 11/12/2016.
 */

public class CardsManager {
    CardBuilder cardBuilder;

    public CardsManager(Context context)
    {
        cardBuilder=new CardBuilder(context);
    }

    View getCard() {
        View cardsView = (View) cardBuilder.makeAndGetCards();
        return cardsView;
    }
}
