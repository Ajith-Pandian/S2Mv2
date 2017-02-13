package com.wowconnect.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wowconnect.R;

import java.util.ArrayList;

/**
 * Created by thoughtchimp on 12/27/2016.
 */


public class GenderSpinnerAdapter extends SpinnerAdapter<String> {
    private ArrayList<String> genderList;

    private Context context;

    public GenderSpinnerAdapter(Context context, int resource,
                                int textViewResourceId, ArrayList<String> objects) {
        super(context, resource, textViewResourceId, objects);
        this.genderList = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,
                        @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView
                    = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spinner, parent, false);
        }

        TextView textView = (TextView) itemView.findViewById(R.id.text_spinner);
        textView.setText(genderList.get(position));
        return itemView;
    }

    @Override
    public int getCount() {
        return genderList.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return genderList.get(position);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                @NonNull ViewGroup parent) {

        View dropDownView = convertView;
        if (dropDownView == null) {
            dropDownView
                    = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spinner, parent, false);
        }
        TextView textView = (TextView) dropDownView.findViewById(R.id.text_spinner);
        textView.setText(genderList.get(position));
        return dropDownView;

    }
}
