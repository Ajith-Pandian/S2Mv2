package com.example.thoughtchimp.s2mconnect.tindercard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.thoughtchimp.s2mconnect.CardBuilder;
import com.example.thoughtchimp.s2mconnect.R;

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
        View cardView = convertView;
        cardBuilder = new CardBuilder(context);
        ViewHolder viewHolder = new ViewHolder();
        if (cardView == null) {

            cardView = cardBuilder.getSwipeCards();
            // configure view holder
            viewHolder.background = (RelativeLayout) cardView.findViewById(R.id.background);
            viewHolder.schoolName = (TextView) cardView.findViewById(R.id.text_school_name);
            viewHolder.message = (TextView) cardView.findViewById(R.id.text_message);
            viewHolder.date = (TextView) cardView.findViewById(R.id.text_date);
            viewHolder.time = (TextView) cardView.findViewById(R.id.text_time);
            viewHolder.likes = (TextView) cardView.findViewById(R.id.text_likes);
            cardView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SchoolDetails details = (SchoolDetails) getItem(position);
        viewHolder.schoolName.setText(details.getName());
        viewHolder.message.setText(details.getMessage());
        viewHolder.date.setText(details.getDate());
        viewHolder.time.setText(details.getTime());
        viewHolder.likes.setText(details.getLikes());
        return cardView;
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
