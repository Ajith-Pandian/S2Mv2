package com.example.uilayer.milestones.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.uilayer.R;
import com.example.uilayer.models.VideoMiles;
import com.google.android.youtube.player.YouTubeIntents;
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
        final VideoMiles mile = milesList.get(position);
        String url = "https://img.youtube.com/vi/" + mile.getVideoId() + "/0.jpg";
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = YouTubeIntents.createPlayVideoIntentWithOptions(context,
                        mile.getVideoId(), true, true);
                context.startActivity(videoIntent);
            }
        });
        Picasso.with(context).load(url).into(holder.mileImage);
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
        ImageButton playButton;

        ViewHolder(View view) {
            super(view);
            mileImage = (ImageView) view.findViewById(R.id.mile_image);
            playButton = (ImageButton) view.findViewById(R.id.button_play);

        }
    }
}