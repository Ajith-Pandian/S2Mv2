package com.example.uilayer.network;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.concurrent.locks.ReadWriteLock;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.SUFFIX_MILES;
import static com.example.domainlayer.Constants.SUFFIX_WOWS;
import static com.example.domainlayer.Constants.TEACHER;

/**
 * Created by thoughtchimp on 12/21/2016.
 */

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.ViewHolder> {

    private List<User> networkProfilesList;
    private Context context;

    public NetworkAdapter(Context context, List<User> networkProfilesList) {
        this.networkProfilesList = networkProfilesList;
        this.context = context;
    }

    User getItem(int position) {
        return networkProfilesList.get(position);
    }

    @Override
    public NetworkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_network, parent, false);

        return new NetworkAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NetworkAdapter.ViewHolder holder, final int position) {
        User user = networkProfilesList.get(position);
        holder.name.setText(user.getName());
        holder.wowText.setText(user.getWow() + SUFFIX_WOWS);
        holder.milesText.setText(user.getMiles() + SUFFIX_MILES);
        holder.textDesignation.setText(TEACHER);
        holder.netwrokLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("com.example.domainlayer.models.User", getItem(position));
                context.startActivity(intent);
            }
        });
        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_profile);
        holder.profileImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.profileImage.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(context, bitmap, 20, 0));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (!user.getAvatar().equals(""))
            Picasso.with(context).load(user.getAvatar()).placeholder(R.drawable.profile).into(target);
    }


    @Override
    public int getItemCount() {
        return networkProfilesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.text_wows)
        TextView wowText;
        @BindView(R.id.text_miles)
        TextView milesText;
        @BindView(R.id.text_designation)
        TextView textDesignation;
        @BindView(R.id.image_profile)
        ImageView profileImage;
        @BindView(R.id.layout_network)
        RelativeLayout netwrokLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
