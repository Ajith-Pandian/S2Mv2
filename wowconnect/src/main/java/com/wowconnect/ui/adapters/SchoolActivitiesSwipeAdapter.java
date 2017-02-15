package com.wowconnect.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wowconnect.NetworkHelper;
import com.wowconnect.R;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.models.SclActs;
import com.wowconnect.ui.customUtils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ajit on 09-02-2017.
 */

public class SchoolActivitiesSwipeAdapter extends ArrayAdapter<SclActs> {
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
    @BindView(R.id.button_like)
    ImageButton likeButton;
    private ArrayList<SclActs> sclActsArrayList;
    private Context context;

    public SchoolActivitiesSwipeAdapter(Context context, ArrayList<SclActs> sclActsArrayList) {
        super(context, 0, sclActsArrayList);
        this.sclActsArrayList = sclActsArrayList;
        //this.context = context;
    }


    @Nullable
    @Override
    public SclActs getItem(int position) {
        return sclActsArrayList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_swipe_school_activity, parent, false);
        }
        ButterKnife.bind(this, convertView);
        final SclActs sclActs = getItem(position);
        schoolActivityMessage.setText(sclActs.getMsg());
        String timeStamp = sclActs.getTimeStamp();
        String date = timeStamp.split(" ")[0];
        String time = timeStamp.split(" ")[1];
        textDate.setText(date);
        textTime.setText(time);
        textLikes.setText(String.valueOf(sclActs.getLikesCount()));
        if (sclActs.isLiked())
            likeButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        else
            likeButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.mile_oolor8));


        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new NetworkHelper(getContext()).likeActivity(sclActs.getId(), new NetworkHelper.LikeListener() {
                    @Override
                    public void onLiked() {
                        likeButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        sclActs.setLikesCount(sclActs.getLikesCount() + 1);
                        sclActs.setLiked(true);
                        saveActivityInDb(sclActs);
                        notifyDataSetChanged();

                    }

                    @Override
                    public void onUnLiked() {
                        likeButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.mile_oolor8));
                        sclActs.setLikesCount(sclActs.getLikesCount() - 1);
                        sclActs.setLiked(false);
                        saveActivityInDb(sclActs);
                        notifyDataSetChanged();
                    }
                });
            }
        });
        Target iconTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(context, bitmap, 20, 1));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (sclActs.getIcon() != null && !sclActs.getIcon().isEmpty())
            Picasso.with(context)
                    .load(sclActs.getIcon())
                    .placeholder(R.drawable.ph_profile)
                    .into(iconTarget);
        else {
            Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_profile);
            imageView.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));
        }
        return convertView;
    }


    private void saveActivityInDb(SclActs schoolActivity) {
        new DataBaseUtil(getContext()).updateSchoolActivity(schoolActivity);
    }
}
