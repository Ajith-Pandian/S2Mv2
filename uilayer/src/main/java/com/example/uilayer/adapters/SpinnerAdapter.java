package com.example.uilayer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.uilayer.R;

import java.util.List;

/**
 * Created by thoughtchimp on 12/26/2016.
 */

class SpinnerAdapter<String> extends ArrayAdapter<String> {
    SpinnerAdapter(Context context, int resource,
                          int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

}
