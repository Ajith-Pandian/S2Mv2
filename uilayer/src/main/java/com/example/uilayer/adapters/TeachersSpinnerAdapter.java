package com.example.uilayer.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.domainlayer.models.User;
import com.example.uilayer.R;

/**
 * Created by thoughtchimp on 12/27/2016.
 */


public class TeachersSpinnerAdapter extends SpinnerAdapter<User> {
    private ArrayList<User> usersList;

    public TeachersSpinnerAdapter(Context context, int resource,
                                  int textViewResourceId, ArrayList<User> objects) {
        super(context, resource, textViewResourceId, objects);
        this.usersList = objects;
    }

    @Override
    public View getView(int position, View convertView,
                        ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView
                    = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spinner, parent, false);
        }

        TextView textView = (TextView) itemView.findViewById(R.id.text_spinner);
        textView.setText(usersList.get(position).getName());
        return itemView;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View dropDownView = convertView;
        if (dropDownView == null) {
            dropDownView
                    = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_spinner, parent, false);
        }
        TextView textView = (TextView) dropDownView.findViewById(R.id.text_spinner);
        textView.setText(usersList.get(position).getName());
        return dropDownView;

    }
}
