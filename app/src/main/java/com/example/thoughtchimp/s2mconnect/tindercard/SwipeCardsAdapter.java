package com.example.thoughtchimp.s2mconnect.tindercard;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thoughtchimp.s2mconnect.R;
import com.example.thoughtchimp.s2mconnect.SwipeCards.CardBuilder;

import java.util.List;

/**
 * Created by thoughtchimp on 11/10/2016.
 */

public class SwipeCardsAdapter extends BaseAdapter {
    private List<SchoolDetails> cardsList;
    private Context context;
    private CardBuilder cardBuilder;

    public SwipeCardsAdapter(List<SchoolDetails> apps, Context context) {
        this.cardsList = apps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cardsList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //View cardView = convertView;
        // cardBuilder = new CardBuilder(context);
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.card_school_update, null);
           // convertView = cardBuilder.makeAndGetCards();
            viewHolder.background = (RelativeLayout) convertView.findViewById(R.id.background);
            viewHolder.schoolName = (TextView) convertView.findViewById(R.id.text_school_name);
            viewHolder.message = (TextView) convertView.findViewById(R.id.text_message);
            viewHolder.date = (TextView) convertView.findViewById(R.id.text_date);
            viewHolder.time = (TextView) convertView.findViewById(R.id.text_time);
            viewHolder.likes = (TextView) convertView.findViewById(R.id.text_likes);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SchoolDetails details = (SchoolDetails) getItem(position);
        viewHolder.schoolName.setText(details.getName());
        viewHolder.message.setText(details.getMessage());
        viewHolder.date.setText(details.getDate());
        viewHolder.time.setText(details.getTime());
        viewHolder.likes.setText(details.getLikes());
        return convertView;
    }

    private static class ViewHolder {
        RelativeLayout background;
        TextView schoolName;
        TextView message;
        TextView date;
        TextView time;
        TextView likes;
    }
}
