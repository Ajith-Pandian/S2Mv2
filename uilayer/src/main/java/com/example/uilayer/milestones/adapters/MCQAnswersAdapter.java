package com.example.uilayer.milestones.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.uilayer.R;
import com.example.uilayer.milestones.MCQActivity;
import com.example.uilayer.models.McqOptions;

import java.util.ArrayList;

/**
 * Created by thoughtchimp on 11/25/2016.
 */

public class MCQAnswersAdapter extends BaseAdapter {
    private ViewHolder holder;
    private ArrayList<McqOptions> optionsList;
    private Context context;

    public MCQAnswersAdapter(Context context, ArrayList<McqOptions> optionsList) {
        this.optionsList = optionsList;
        this.context = context;
    }

    @Override
    public McqOptions getItem(int position) {
        return optionsList.get(position);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View convertView = view;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_mcq_answer, viewGroup, false);
            holder = new ViewHolder();
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.image_button_mcq);
            holder.textAnswer = (TextView) convertView.findViewById(R.id.text_answer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        McqOptions options = optionsList.get(i);
        holder.textAnswer.setText(options.getText());
        resetStates();
        if (options.isSelected()) {
            holder.imageButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.backround_circle_green_primary));
            holder.imageButton.setImageDrawable(null);
        } else if (options.isRight()) {
            holder.imageButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.backround_circle_green_primary));
            holder.imageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white_16dp));
            holder.textAnswer.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (options.isWrong()) {
            holder.imageButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.backround_circle_red));
            holder.imageButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_white_16dp));
            holder.textAnswer.setTextColor(context.getResources().getColor(R.color.red4));
        } else {
            holder.imageButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.backround_circle_stroke_colr_sec));
            holder.imageButton.setImageDrawable(null);
        }
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MCQActivity)context).onClickRow(i);
            }
        });
        return convertView;
    }


    public void resetStates() {
        holder.imageButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.backround_circle_stroke_colr_sec));
        holder.imageButton.setImageDrawable(null);
        holder.textAnswer.setTextColor(context.getResources().getColor(R.color.text_secondary));
    }

    @Override
    public int getCount() {
        return optionsList.size();
    }

    class ViewHolder {
        ImageButton imageButton;
        TextView textAnswer;
    }

}
