package com.example.uilayer.manage;

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

import com.example.domainlayer.models.Schools;
import com.example.domainlayer.models.User;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.network.ProfileActivity;
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

public class SchoolsAdapter extends RecyclerView.Adapter<SchoolsAdapter.ViewHolder> {

    private List<Schools> schoolsList;
    private Context context;

    public SchoolsAdapter(Context context, List<Schools> schoolsList) {
        this.schoolsList = schoolsList;
        this.context = context;
    }

    Schools getItem(int position) {
        return schoolsList.get(position);
    }

    @Override
    public SchoolsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_school, parent, false);

        return new SchoolsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SchoolsAdapter.ViewHolder holder, final int position) {
        Schools school = schoolsList.get(position);
        holder.schoolName.setText(school.getName());
        holder.schoolAddress.setText(school.getAddress());

        holder.schoolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_schools_small);
        holder.schollImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.schollImage.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(context, bitmap, 20, 0));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (school.getLogo() != null && !school.getLogo().equals(""))
            Picasso.with(context)
                    .load(school.getLogo())
                    .resize(50, 50)
                    .placeholder(R.drawable.profile)
                    .into(target);
    }


    @Override
    public int getItemCount() {
        return schoolsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_school_name)
        TextView schoolName;
        @BindView(R.id.text_school_address)
        TextView schoolAddress;
        @BindView(R.id.image_school)
        ImageView schollImage;
        @BindView(R.id.layout_school)
        RelativeLayout schoolLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
