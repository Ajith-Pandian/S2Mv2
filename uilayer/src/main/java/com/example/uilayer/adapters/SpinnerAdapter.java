package com.example.uilayer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.uilayer.R;

import java.util.List;

/**
 * Created by thoughtchimp on 12/26/2016.
 */

public class SpinnerAdapter<String> extends ArrayAdapter<String> {
    public SpinnerAdapter(Context context, int resource,
                          int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public View getView(int position, View convertView,
                        ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        // apply the style and sizes etc to the Text view from this view v
        // like ((TextView)v).setTextSize(...) etc
        return v;
    }

    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        // this is for each of the drop down resource that is created.
        // you can style these things too
        View v = super.getView(position, convertView, parent);
        // apply the style and sizes etc to the Text view from this view v
        // like ((TextView)v).setTextSize(...) etc
        return v;
    }
}
