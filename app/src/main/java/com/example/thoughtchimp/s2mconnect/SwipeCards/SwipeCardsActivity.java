package com.example.thoughtchimp.s2mconnect.SwipeCards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.thoughtchimp.s2mconnect.R;
import com.example.thoughtchimp.s2mconnect.tindercard.SchoolDetails;

import java.util.ArrayList;

public class SwipeCardsActivity extends AppCompatActivity {
    SwipeLayout swipeLayout;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_cards);
        ArrayList<SchoolDetails> strings = new ArrayList<>();
        strings = new ArrayList<>();
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "09.11.2016", "2.15 PM", "123"));
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_two),
                "09.12.2017", "7.26 AM", "456"));
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "10.11.2018", "2.46 AM", "789"));
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_two),
                "09.11.2019", "5.32 PM", "102"));
        strings.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "09.11.2020", "9.05 PM", "098"));
        swipeLayout= (SwipeLayout) findViewById(R.id.swipe_layout);
        CardsView swipeCardsView = (CardsView) findViewById(R.id.swipe_cards);
        SwipeLayoutAdapter adapter = new SwipeLayoutAdapter(getApplicationContext(), strings);
        swipeLayout.setAdapter(adapter);
        swipeLayout.setSwipeCardsView(swipeCardsView);
        button=(Button)findViewById(R.id.next_button);
    button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.addNextCard();
            }
        });
       // button.setText("AAAAAA");

    }
}
