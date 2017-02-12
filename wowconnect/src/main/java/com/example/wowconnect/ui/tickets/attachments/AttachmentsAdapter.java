package com.example.wowconnect.ui.tickets.attachments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wowconnect.R;

/**
 * Created by thoughtchimp on 1/23/2017.
 */

public class AttachmentsAdapter extends BaseAdapter {

   private Context context;
    private String [] names;
    private int [] iconIds;
    public AttachmentsAdapter(Context context,String [] names,int [] iconIds) {
        this.context = context;
        this.names = names;
        this.iconIds = iconIds;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return names[position];
    }

    @Override
    public long getItemId(int position) {
        return iconIds[position];
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View convertView = view;
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_attachment, viewGroup, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.text_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon_attachment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(names[position]);
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context,iconIds[position]));
        return convertView;
    }


    class ViewHolder {
        TextView name;
        ImageView icon;
    }
}
