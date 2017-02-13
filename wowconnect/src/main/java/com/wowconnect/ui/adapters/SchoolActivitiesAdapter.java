package com.wowconnect.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowconnect.NetworkHelper;
import com.wowconnect.R;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.models.SclActs;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 11/22/2016.
 */

public class SchoolActivitiesAdapter extends RecyclerView.Adapter<SchoolActivitiesAdapter.ViewHolder> {

    private List<SclActs> sclActsList;
    private Context context;

    public SchoolActivitiesAdapter(Context context, List<SclActs> sclActsList) {
        this.sclActsList = sclActsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_school_activity, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SclActs sclActs = sclActsList.get(position);
        holder.schoolActivityMessage.setText(sclActs.getMsg());
        holder.textTimestamp.setText(sclActs.getTimeStamp());
        holder.textLikes.setText("" + sclActs.getLikesCount());
        if (sclActs.isLiked())
            //changeColor(holder.likeButton, R.color.colorPrimary);
            holder.likeButton.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
        else
            //changeColor(holder.likeButton, R.color.mile_oolor8);
            holder.likeButton.setColorFilter(context.getResources().getColor(R.color.mile_oolor8));
        if (sclActs.getIcon() != null && !sclActs.getIcon().isEmpty())
            Picasso.with(context).load(sclActs.getIcon()).into(holder.imageView);
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetworkHelper(context).likeActivity(sclActs.getId(), new NetworkHelper.LikeListener() {
                    @Override
                    public void onLiked() {
                       // changeColor(holder.likeButton, R.color.colorPrimary);
                        holder.likeButton.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
                        sclActs.setLikesCount(sclActs.getLikesCount() + 1);
                        sclActs.setLiked(true);
                        saveActivityInDb(sclActs);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onUnLiked() {
                        //changeColor(holder.likeButton, R.color.mile_oolor8);
                        holder.likeButton.setColorFilter(context.getResources().getColor(R.color.mile_oolor8));
                        sclActs.setLikesCount(sclActs.getLikesCount() - 1);
                        sclActs.setLiked(false);
                        saveActivityInDb(sclActs);
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void changeColor(ImageButton button, int color) {
        button.setColorFilter(context.getResources().getColor(color));
    }

    private void saveActivityInDb(SclActs schoolActivity) {
        new DataBaseUtil(context).updateSchoolActivity(schoolActivity);
    }

    @Override
    public int getItemCount() {
        return sclActsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_message)
        TextView schoolActivityMessage;
        @BindView(R.id.timestamp)
        TextView textTimestamp;
        @BindView(R.id.text_likes)
        TextView textLikes;
        @BindView(R.id.image_school_activity)
        ImageView imageView;
        @BindView(R.id.button_like)
        ImageButton likeButton;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
