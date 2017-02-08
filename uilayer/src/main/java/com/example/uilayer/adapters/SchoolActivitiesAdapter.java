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

import com.example.domainlayer.models.SclActs;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        SclActs sclActs = sclActsList.get(position);
        holder.schoolName.setText(sclActs.getTitle());
        holder.schoolActivityMessage.setText(sclActs.getMsg());
        String timpeStamp=sclActs.getTimeStamp();
        String date=timpeStamp.split(" ")[0];
        String time=timpeStamp.split(" ")[1];
        holder.textDate.setText(date);
        holder.textTime.setText(time);
        holder.textLikes.setText(""+sclActs.getLikesCount());
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.user_image);
        holder.imageView.setImageDrawable(Utils.getInstance().getCirclularImage(context, imageBitmap));
    }

    @Override
    public int getItemCount() {
        return sclActsList.size();
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
