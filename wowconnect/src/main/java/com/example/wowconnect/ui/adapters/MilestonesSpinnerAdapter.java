package com.example.wowconnect.ui.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.wowconnect.R;
import com.example.wowconnect.models.Milestones;

import java.util.ArrayList;

/**
 * Created by thoughtchimp on 12/27/2016.
 */


public class MilestonesSpinnerAdapter extends SpinnerAdapter<Milestones> {
    private ArrayList<Milestones> milestonesArrayList;
    private int textViewResourceId;

    public MilestonesSpinnerAdapter(Context context, int resource,
                                    int textViewResourceId, ArrayList<Milestones> objects) {
        super(context, resource, textViewResourceId, objects);
        this.milestonesArrayList = objects;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {

        View promptView = convertView;
        if (promptView == null) {
            promptView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spinner, parent, false);
        }
        TextView textView = (TextView) promptView.findViewById(R.id.text_spinner);
        textView.setText(milestonesArrayList.get(position).getName());
        return promptView;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        View dropDownView = convertView;
        if (dropDownView == null) {
            dropDownView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spinner, parent, false);
        }
        TextView textView = (TextView) dropDownView.findViewById(R.id.text_spinner);
        textView.setText(milestonesArrayList.get(position).getName());
        return dropDownView;

    }

    @Nullable
    @Override
    public Milestones getItem(int position) {
        return milestonesArrayList.get(position);
    }

    @Override
    public int getCount() {
        return milestonesArrayList.size();
    }

}
