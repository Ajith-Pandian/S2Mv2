package com.example.uilayer.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.domainlayer.models.Milestones;
import com.example.domainlayer.models.User;
import com.example.uilayer.R;

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

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spinner, parent, false);

        TextView textView = (TextView) itemView.findViewById(R.id.text_spinner);
        textView.setText(milestonesArrayList.get(position).getName());
        return itemView;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View dropDownView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spinner, parent, false);
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
