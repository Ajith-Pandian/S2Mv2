package com.example.uilayer.milestones;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.milestones.betterAdapter.model.Milestones;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class MilesAdapter extends RecyclerView.Adapter<MilesAdapter.ViewHolder> {

    private List<Milestones> milestonesList;
    private Context context;

    public MilesAdapter(Context context, List<Milestones> milestonesList) {
        this.milestonesList = milestonesList;
        this.context = context;
    }

    @Override
    public MilesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 1)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_miles, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_trainings, parent, false);
        return new MilesAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MilesAdapter.ViewHolder holder, int position) {
        Milestones milestones = milestonesList.get(position);
     /*   holder.schoolName.setText(milestones.getName());
        holder.schoolActivityMessage.setText(milestones.getMessage());
        holder.textTime.setText(schoolDetail.getTime());
        holder.textDate.setText(schoolDetail.getDate());
        holder.textLikes.setText(schoolDetail.getLikes());*/
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_image);
        holder.imageView.setImageDrawable(Utils.getInstance().getCirclularImage(context, imageBitmap));
    }

    @Override
    public int getItemCount() {
        return milestonesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return milestonesList.get(position).getType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_school_name)
        TextView schoolName;
        @BindView(R.id.text_message)
        TextView schoolActivityMessage;
        @BindView(R.id.text_time)
        TextView textTime;
        @BindView(R.id.text_date)
        TextView textDate;
        @BindView(R.id.text_likes)
        TextView textLikes;
        @BindView(R.id.image_school_activity)
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
