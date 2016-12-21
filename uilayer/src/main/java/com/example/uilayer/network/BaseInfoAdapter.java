package com.example.uilayer.network;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.domainlayer.models.User;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.SUFFIX_MILES;
import static com.example.domainlayer.Constants.SUFFIX_WOWS;
import static com.example.domainlayer.Constants.TEACHER;

/**
 * Created by thoughtchimp on 12/21/2016.
 */

public class BaseInfoAdapter extends RecyclerView.Adapter<BaseInfoAdapter.ViewHolder> {

    private List<ProfileModel> profileModelList;
    private Context context;

    public BaseInfoAdapter(Context context, List<ProfileModel> networkProfilesList) {
        this.profileModelList = networkProfilesList;
        this.context = context;
    }

    ProfileModel getItem(int position) {
        return profileModelList.get(position);
    }

    @Override
    public BaseInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_basic_profile, parent, false);

        return new BaseInfoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BaseInfoAdapter.ViewHolder holder, final int position) {
        ProfileModel profile = profileModelList.get(position);
        holder.primaryText.setText(profile.getPrimaryText());
        holder.secondaryText.setText(profile.getSecondaryText());
        holder.imageIcon.setImageDrawable(context.getResources().getDrawable(profile.getIconId()));
    }


    @Override
    public int getItemCount() {
        return profileModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_primary)
        TextView primaryText;
        @BindView(R.id.text_secondary)
        TextView secondaryText;
        @BindView(R.id.icon)
        ImageView imageIcon;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}

