package com.example.uilayer.milestones.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.MCQCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by thoughtchimp on 11/25/2016.
 */

public class MCQAnswersAdapter extends BaseAdapter {
    private final ArrayList mData;
    private ViewHolder holder;
    private Map<String, String> optionsList;
    private Context context;

    public MCQAnswersAdapter(Context context, Map<String, String> optionsList) {
        this.optionsList = optionsList;
        this.context = context;
        mData = new ArrayList();
        mData.addAll(optionsList.entrySet());
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_mcq_answer, viewGroup, false);
            holder = new ViewHolder();
            holder.mcqCheckBox = (MCQCheckBox) convertView.findViewById(R.id.checkbox_mcq);
            holder.textAnswer = (TextView) convertView.findViewById(R.id.text_answer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textAnswer.setText(getItem(i).getValue());
        setStates(i);
        return convertView;
    }

    public void setStates(int position) {
        resetStates();
        switch (position) {
            case 0:
                holder.mcqCheckBox.setSelected(true);
                break;
            case 1:
                holder.mcqCheckBox.setNotSelected(true);
                break;
            case 2:
                holder.mcqCheckBox.setRight(true);
                holder.mcqCheckBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_white_20dp));
                holder.textAnswer.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                break;
            case 3:
                holder.mcqCheckBox.setWrong(true);
                holder.mcqCheckBox.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_white_24dp));
                holder.textAnswer.setTextColor(context.getResources().getColor(R.color.red));
                break;
        }
    }

    private void resetStates() {
        holder.mcqCheckBox.setNotSelected(false);
        holder.mcqCheckBox.setSelected(false);
        holder.mcqCheckBox.setRight(false);
        holder.mcqCheckBox.setWrong(false);
        holder.textAnswer.setTextColor(context.getResources().getColor(R.color.text_secondary));

    }

    @Override
    public int getCount() {
        return optionsList.size();
    }

    class ViewHolder {
        MCQCheckBox mcqCheckBox;
        TextView textAnswer;
    }
}
