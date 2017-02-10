package com.example.uilayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.domainlayer.models.SclActs;
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;

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

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_swipe_school_activity, parent, false);
        }
        ButterKnife.bind(this, convertView);
        final SclActs sclActs = sclActsArrayList.get(position);
        schoolActivityMessage.setText(sclActs.getMsg());
        String timeStamp = sclActs.getTimeStamp();
        String date = timeStamp.split(" ")[0];
        String time = timeStamp.split(" ")[1];
        textDate.setText(date);
        textTime.setText(time);
        textLikes.setText("" + sclActs.getLikesCount());
        if (sclActs.isLiked())
            //changeColor( likeButton, R.color.colorPrimary);
            likeButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        else
            //changeColor( likeButton, R.color.mile_oolor8);
            likeButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.mile_oolor8));

        Bitmap imageBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.user_image);
        imageView.setImageDrawable(Utils.getInstance().getCirclularImage(getContext(), imageBitmap));
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new NetworkHelper(getContext()).likeActivity(sclActs.getId(), new NetworkHelper.LikeListener() {
                    @Override
                    public void onLiked() {
                        ((ImageButton) v).setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        sclActs.setLikesCount(sclActs.getLikesCount() + 1);
                        sclActs.setLiked(true);
                        notifyDataSetInvalidated();
                        notifyDataSetChanged();

                    }

                    @Override
                    public void onUnLiked() {
                        likeButton.setColorFilter(ContextCompat.getColor(getContext(), R.color.mile_oolor8));
                        sclActs.setLikesCount(sclActs.getLikesCount() - 1);
                        sclActs.setLiked(false);
                        notifyDataSetChanged();
                    }
                });
            }
        });
        return convertView;
    }
}
