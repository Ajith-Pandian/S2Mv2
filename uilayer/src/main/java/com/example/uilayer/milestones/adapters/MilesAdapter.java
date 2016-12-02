package com.example.uilayer.milestones.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uilayer.R;
import com.example.uilayer.milestones.MilesActivity;
import com.example.uilayer.milestones.TrainingActivity;
import com.example.uilayer.milestones.betterAdapter.model.Mile;
import com.example.uilayer.milestones.betterAdapter.model.Milestones;
import com.example.uilayer.milestones.betterAdapter.model.Training;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class MilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Milestones> milestonesList;
    private Context context;

    public MilesAdapter(Context context, List<Milestones> milestonesList) {
        this.milestonesList = milestonesList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_miles, parent, false);
                return new MilesViewHolder(itemView);
            case 2:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_trainings, parent, false);
                return new TrainingsViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_miles, parent, false);
                return new MilesViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                MilesViewHolder viewHolder = (MilesViewHolder) holder;
                Mile mile = (Mile) milestonesList.get(position);
                viewHolder.title.setText(mile.getTitle());
                viewHolder.textContent.setText(mile.getContent());
                viewHolder.textPosition.setText("" + mile.getPosition());
                viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity(MilesActivity.class);
                    }
                });
                break;
            case 2:
                TrainingsViewHolder tViewHolder = (TrainingsViewHolder) holder;
                Training training = (Training) milestonesList.get(position);
                tViewHolder.title.setText(training.getTitle());
                tViewHolder.textContent.setText(training.getContent());
                tViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openActivity(TrainingActivity.class);
                    }
                });
                break;
        }

    }

    private void openActivity(Class<?> activityClass)
    {
        Intent intent=new Intent(context,activityClass);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return milestonesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return milestonesList.get(position).getType();
    }

    class MilesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title_type)
        TextView type;
        @BindView(R.id.text_title)
        TextView title;
        @BindView(R.id.text_content)
        TextView textContent;
        @BindView(R.id.text_big_number)
        TextView textPosition;
        @BindView(R.id.layout_root_miles)
        RelativeLayout rootLayout;

        MilesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class TrainingsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title_type)
        TextView type;
        @BindView(R.id.text_title)
        TextView title;
        @BindView(R.id.text_content)
        TextView textContent;
        @BindView(R.id.layout_root_trainings)
        RelativeLayout rootLayout;

        public TrainingsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
