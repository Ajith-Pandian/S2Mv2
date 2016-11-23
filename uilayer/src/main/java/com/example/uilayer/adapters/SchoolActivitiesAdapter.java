package com.example.uilayer.adapters;

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
import com.example.uilayer.models.SchoolDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 11/22/2016.
 */

public class SchoolActivitiesAdapter extends RecyclerView.Adapter<SchoolActivitiesAdapter.ViewHolder> {

    private List<SchoolDetails> schoolDetailsList;
    private Context context;

    public SchoolActivitiesAdapter(Context context, List<SchoolDetails> schoolDetailsList) {
        this.schoolDetailsList = schoolDetailsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_school_activity, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SchoolDetails schoolDetail = schoolDetailsList.get(position);
        holder.schoolName.setText(schoolDetail.getName());
        holder.schoolActivityMessage.setText(schoolDetail.getMessage());
        holder.textTime.setText(schoolDetail.getTime());
        holder.textDate.setText(schoolDetail.getDate());
        holder.textLikes.setText(schoolDetail.getLikes());
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_image);
        holder.imageView.setImageDrawable(Utils.getInstance().getCirclularImage(context, imageBitmap));
    }

    @Override
    public int getItemCount() {
        return schoolDetailsList.size();
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
