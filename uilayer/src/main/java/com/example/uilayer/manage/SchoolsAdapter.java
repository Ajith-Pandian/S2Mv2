package com.example.uilayer.manage;

import android.app.Activity;
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
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.R;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.adapters.SectionsAdapter;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.landing.LandingActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 12/21/2016.
 */

public class SchoolsAdapter extends RecyclerView.Adapter<SchoolsAdapter.ViewHolder> {

    private List<Schools> schoolsList;
    private Context context;
    private boolean isFirstTime;

    public SchoolsAdapter(Context context, List<Schools> schoolsList, boolean isFirstTime) {
        this.schoolsList = schoolsList;
        this.context = context;
        this.isFirstTime = isFirstTime;
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
        final Schools school = schoolsList.get(position);
        holder.schoolName.setText(school.getName());
        holder.schoolAddress.setText(school.getLocality());

        updateActiveSchoolVisibility(position, holder);
        holder.schoolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveSchool(school);
                updateActiveSchoolVisibility(position, holder);
                notifyDataSetChanged();
                if (isFirstTime)
                    launchManageTeachersAndSections();
                else
                    launchLandingActivity();
                ((Activity) context).finish();
            }
        });
        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_schools_small);
        holder.schoolImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.schoolImage.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(context, bitmap, 20, 0));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (!school.getLogo().equals(""))
            Picasso.with(context)
                    .load(school.getLogo())
                    .resize(50, 50)
                    .placeholder(R.drawable.profile)
                    .into(target);
    }


    private void setActiveSchool(Schools school) {
        removeOldActiveSchool();
        school.setActive(true);
        SharedPreferenceHelper.setSchoolName(school.getName());
        SharedPreferenceHelper.setSchoolId(school.getId());
        if (!isFirstTime)
            Utils.getInstance().showToast("School Changed");
    }

    private void removeOldActiveSchool() {
        for (Schools school : schoolsList) {
            school.setActive(false);
        }
    }

    private void updateActiveSchoolVisibility(int position, ViewHolder holder) {
        boolean isActive = schoolsList.get(position).isActive();

        int color = context.getResources().getColor(isActive ? R.color.colorPrimary : android.R.color.black);
        holder.schoolName.setTextColor(color);
        holder.schoolAddress.setTextColor(color);
        holder.activeBadge.setVisibility(isActive ? View.VISIBLE : View.GONE);
    }

    private void launchLandingActivity() {
        new NetworkHelper(context).getDashBoardDetails(new NetworkHelper.NetworkListener() {
            @Override
            public void onFinish() {
                ((Activity) context).finish();
                context.startActivity(new Intent(context, LandingActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    private void launchManageTeachersAndSections() {
        context.startActivity(new Intent(context, ManageTeachersActivity.class)
                .putExtra("isFirstTime", true)
                .putExtra("isTeachers", true)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
        @BindView(R.id.text_active_badge)
        TextView activeBadge;
        @BindView(R.id.image_school)
        ImageView schoolImage;

        @BindView(R.id.layout_school)
        RelativeLayout schoolLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
