package com.wowconnect.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wowconnect.R;
import com.wowconnect.domain.Constants;
import com.wowconnect.models.DbUser;

import java.util.ArrayList;


/**
 * Created by thoughtchimp on 12/27/2016.
 */


public class TeachersSpinnerAdapter extends SpinnerAdapter<DbUser> {
    private ArrayList<DbUser> usersList;

    public TeachersSpinnerAdapter(Context context, int resource,
                                  int textViewResourceId, ArrayList<DbUser> objects) {
        super(context, resource, textViewResourceId, objects);
        this.usersList = objects;
    }

    @NonNull
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
        DbUser user = usersList.get(position);
        String name = user.getFirstName() + Constants.SPACE + user.getLastName();
        textView.setText(name);
        return itemView;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Nullable
    @Override
    public DbUser getItem(int position) {
        return usersList.get(position);
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
        DbUser user = usersList.get(position);
        String fullName = user.getFirstName() + Constants.SPACE + user.getLastName();
        textView.setText(fullName);
        return dropDownView;

    }
}
