package com.wowconnect.ui.landing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Created by Ajit on 16-02-2017.
 */

public class SchoolPagerAdapter extends PagerAdapter {

    @BindView(R.id.text_message)
    TextView schoolActivityMessage;
    @BindView(R.id.timestamp)
    TextView textTimestamp;
    @BindView(R.id.text_likes)
    TextView textLikes;
    @BindView(R.id.image_school_activity)
    ImageView imageView;
    @BindView(R.id.image_like)
    AppCompatImageView likeImage;
    @BindView(R.id.layout_like)
    LinearLayout likeLayout;
    Context context;
    LayoutInflater mLayoutInflater;
    ArrayList<SclActs> sclActsArrayList;

    public SchoolPagerAdapter(Context context, ArrayList<SclActs> sclActsArrayList) {
        this.context = context;
        this.sclActsArrayList = sclActsArrayList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sclActsArrayList.size() + 1;
    }

    private static final String TAG = "SchoolPagerAdapter";

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View itemView;
        if (position != getCount() - 1) {
            itemView = mLayoutInflater.inflate(R.layout.item_school_activitypager, container, false);
            ButterKnife.bind(this, itemView);
            loadAdapterData(position);
        } else {
            itemView = mLayoutInflater.inflate(R.layout.item_add_card, container, false);
            CardView cardView = (CardView) itemView.findViewById(R.id.layout_last_card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, SchoolDetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }

        container.addView(itemView);
        return itemView;
    }

    private NetworkHelper networkHelper;

    void loadAdapterData(int position) {
        final SclActs sclActs = sclActsArrayList.get(position);
        schoolActivityMessage.setText(sclActs.getMsg());
        textTimestamp.setText(sclActs.getTimeStamp());
        textLikes.setText("" + sclActs.getLikesCount());
        if (sclActs.isLiked())
            //changeColor( likeButton, R.color.colorPrimary);
            likeImage.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
        else
            //changeColor( likeButton, R.color.mile_oolor8);
            likeImage.setColorFilter(context.getResources().getColor(R.color.mile_oolor8));

        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkHelper = new NetworkHelper(context);
                networkHelper.likeActivity(sclActs.getId(), new NetworkHelper.LikeListener() {
                    @Override
                    public void onLiked() {
                        // changeColor( likeButton, R.color.colorPrimary);
                        likeImage.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
                        sclActs.setLikesCount(sclActs.getLikesCount() + 1);
                        sclActs.setLiked(true);
                        saveActivityInDb(sclActs);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onUnLiked() {
                        //changeColor( likeButton, R.color.mile_oolor8);
                        likeImage.setColorFilter(context.getResources().getColor(R.color.mile_oolor8));
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

    }

    private void saveActivityInDb(SclActs schoolActivity) {
        new DataBaseUtil(context).updateSchoolActivity(schoolActivity);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}

