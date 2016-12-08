package com.example.uilayer.milestones.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.uilayer.R;
import com.example.uilayer.models.ImageMiles;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 11/14/2016.
 */

public class ImageMilesAdapter extends RecyclerView.Adapter<ImageMilesAdapter.ViewHolder> {

    final float last_item_view_percentage = 0.5f;
    private List<ImageMiles> milesList;
    private Context context;

    public ImageMilesAdapter(Context context, List<ImageMiles> milesList) {
        this.milesList = milesList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_miles, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = (int) (parent.getMeasuredWidth() / (2.6));
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageMiles mile = milesList.get(position);
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
        @BindView(R.id.mile_image_image_miles)
        ImageView mileImage;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);

        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}