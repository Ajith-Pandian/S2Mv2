package com.example.wowconnect.ui.milestones.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wowconnect.R;
import com.example.wowconnect.models.miles.AudioMiles;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 11/14/2016.
 */

public class AudioMilesAdapter extends RecyclerView.Adapter<AudioMilesAdapter.ViewHolder> {

    final float last_item_view_percentage = 0.5f;
    private List<AudioMiles> milesList;
    private Context context;

    public AudioMilesAdapter(Context context, List<AudioMiles> milesList) {
        this.milesList = milesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_audio_miles, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = (int) (parent.getMeasuredWidth() / (2.6));
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AudioMiles mile = milesList.get(position);
        Picasso.with(context)
                .load(mile.getImage_url())
                .placeholder(R.drawable.ph_audio)
                .into(holder.mileImage);
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
        @BindView(R.id.mile_image_audio_miles)
        ImageView mileImage;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }
}