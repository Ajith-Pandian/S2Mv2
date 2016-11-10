package com.example.thoughtchimp.s2mconnect;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thoughtchimp.s2mconnect.tindercard.SchoolDetails;
import com.example.thoughtchimp.s2mconnect.tindercard.SwipeCardsAdapter;
import com.example.thoughtchimp.s2mconnect.tindercard.SwipeFlingAdapterView;
import com.huxq17.swipecardsview.SwipeCardsView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by thoughtchimp on 11/7/2016.
 */

public class HomeFragment extends Fragment {

    SwipeFlingAdapterView flingContainer;
    ArrayList<SchoolDetails> al;
    SwipeCardsAdapter myAppAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        flingContainer = (SwipeFlingAdapterView) view.findViewById(R.id.frame);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                flingContainer.onTouchEvent(motionEvent);
                return false;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showCards();
    }



    void showCards() {
        al = new ArrayList<>();
        al.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "09.11.2016", "2.15 PM", "123"));
        al.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_two),
                "09.12.2017", "7.26 AM", "456"));
        al.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "10.11.2018", "2.46 AM", "789"));
        al.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_two),
                "09.11.2019", "5.32 PM", "102"));
        al.add(new SchoolDetails("St.Jhons Hr Sec School", getResources().getString(R.string.school_msg_one),
                "09.11.2020", "9.05 PM", "098"));

        myAppAdapter = new SwipeCardsAdapter(al, getActivity());
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                reloadItems();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                reloadItems();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                Log.d(TAG, "onAdapterAboutToEmpty: " + itemsInAdapter);
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                if (flingContainer.getSelectedView() != null) {
                    View view = flingContainer.getSelectedView();
                   // view.findViewById(R.id.backgrounds).setAlpha(0);
                }
            }
        });


        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {


            }
        });

    }

    void reloadItems() {
        SchoolDetails temp = al.get(0);
        al.remove(0);
        myAppAdapter.notifyDataSetChanged();
        al.add(temp);
    }




}