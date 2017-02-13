package com.wowconnect.ui.milestones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wowconnect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thoughtchimp on 11/25/2016.
 */

public class OptionsAdapter extends BaseAdapter {
    private List<String> optionsList;
    private Context context;

    public OptionsAdapter(Context context, ArrayList<String> optionsList) {
        this.optionsList = optionsList;
        this.context = context;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = view;
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_options_feedback, viewGroup, false);
            holder = new ViewHolder();
            holder.options = (TextView) convertView.findViewById(R.id.text_options);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.options.setText(optionsList.get(i));
        holder.options.setActivated(false);
        return convertView;
    }

    @Override
    public int getCount() {
        return optionsList.size();
    }

    class ViewHolder {
        TextView options;
    }
}
