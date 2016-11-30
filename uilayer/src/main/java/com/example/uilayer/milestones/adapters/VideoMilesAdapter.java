package com.example.uilayer.milestones.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.uilayer.R;
import com.example.uilayer.models.VideoMiles;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by thoughtchimp on 11/14/2016.
 */

public class VideoMilesAdapter extends RecyclerView.Adapter<VideoMilesAdapter.ViewHolder> {

    final float last_item_view_percentage = 0.5f;
    private List<VideoMiles> milesList;
    private Context context;

    public VideoMilesAdapter(Context context, List<VideoMiles> milesList) {
        this.milesList = milesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_miles, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = (int) (parent.getMeasuredWidth() / (2.6));
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoMiles mile = milesList.get(position);
        Picasso.with(context).load(mile.getUrl()).into(holder.mileImage);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return milesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mileImage;

        ViewHolder(View view) {
            super(view);
            mileImage = (ImageView) view.findViewById(R.id.mile_image);

        }
    }
}