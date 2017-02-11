package com.example.uilayer.network;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.domainlayer.Constants;
import com.example.domainlayer.models.DbUser;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.NewDataParser;
import com.example.uilayer.R;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.domainlayer.Constants.ROLE_COORDINATOR;
import static com.example.domainlayer.Constants.ROLE_SCL_ADMIN;
import static com.example.domainlayer.Constants.ROLE_TEACHER;
import static com.example.domainlayer.Constants.SUFFIX_MILES;
import static com.example.domainlayer.Constants.SUFFIX_WOWS;
import static com.example.domainlayer.Constants.TEXT_COORDINATOR;
import static com.example.domainlayer.Constants.TEXT_S2M_ADMIN;
import static com.example.domainlayer.Constants.TEXT_SCL_ADMIN;
import static com.example.domainlayer.Constants.TEXT_TEACHER;
import static com.example.domainlayer.Constants.USER_TYPE_S2M_ADMIN;
import static com.example.domainlayer.Constants.USER_TYPE_SCHOOL;

/**
 * Created by thoughtchimp on 12/21/2016.
 */

public class NetworkAdapter extends RecyclerView.Adapter<NetworkAdapter.ViewHolder> {

    private List<DbUser> networkProfilesList;
    private Context context;

    public NetworkAdapter(Context context, List<DbUser> networkProfilesList) {
        this.networkProfilesList = networkProfilesList;
        this.context = context;
    }

    DbUser getItem(int position) {
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
        final DbUser user = networkProfilesList.get(position);
        holder.name.setText(user.getFirstName() + " " + user.getLastName());
        holder.wowText.setText(user.getWow() + SUFFIX_WOWS);
        holder.milesText.setText(user.getMiles() + SUFFIX_MILES);

        String userType = user.getType();
        if (userType.equals(USER_TYPE_S2M_ADMIN)) {
            holder.textDesignation.setText(TEXT_S2M_ADMIN);
            holder.wowLayout.setVisibility(View.GONE);

        } else if (userType.equals(USER_TYPE_SCHOOL)) {
            //ArrayList<String> roles = user.getRoles();
            ArrayList<String> roles = new NewDataParser().getUserRoles(context, user.getId());

            if (roles.contains(ROLE_COORDINATOR))
                holder.textDesignation.setText(TEXT_COORDINATOR);
            else if (roles.contains(ROLE_SCL_ADMIN))
                holder.textDesignation.setText(TEXT_SCL_ADMIN);
            else if (roles.contains(ROLE_TEACHER))
                holder.textDesignation.setText(TEXT_TEACHER);
        }


        if (user.getId() == SharedPreferenceHelper.getUserId())
            holder.callButton.setVisibility(View.GONE);
        else {
            holder.callButton.setVisibility(View.VISIBLE);
            holder.callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeCall(user.getPhoneNum());
                }
            });
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewDataHolder.getInstance(context).setCurrentNetworkUser(user);
                Intent intent = new Intent(context, NetworkProfileActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
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

    private void makeCall(String phoneNumber) {
        Intent in = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+" + Constants.COUNTRY_CODE + phoneNumber));
        try {
            in.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "Could not find an activity to place the call.", Toast.LENGTH_SHORT).show();
        }
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
        @BindView(R.id.button_call)
        ImageButton callButton;
        @BindView(R.id.layout_network)
        RelativeLayout rootLayout;
        @BindView(R.id.layout_wows)
        LinearLayout wowLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
