package com.wowconnect.ui.network;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowconnect.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

