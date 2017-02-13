package com.wowconnect.ui.network;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wowconnect.R;
import com.wowconnect.models.Sections;
import com.wowconnect.ui.customUtils.views.CustomProgressBar;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 12/28/2016.
 */

public class ProfileSectionsAdapter extends RecyclerView.Adapter<ProfileSectionsAdapter.ViewHolder> {

    private List<Sections> sectionsList;
    private Context context;
    private int[] colorsArray = {R.color.mile_oolor1, R.color.mile_oolor2, R.color.mile_oolor3,
            R.color.mile_oolor4, R.color.mile_oolor5, R.color.mile_oolor6};

    public ProfileSectionsAdapter(Context context, List<Sections> networkProfilesList) {
        this.sectionsList = networkProfilesList;
        this.context = context;
    }

    Sections getItem(int position) {
        return sectionsList.get(position);
    }

    @Override
    public ProfileSectionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_sections, parent, false);

        return new ProfileSectionsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProfileSectionsAdapter.ViewHolder holder, final int position) {
        Sections sections = sectionsList.get(position);
        holder.classAndSectionText.setText(sections.get_Class() + " " + sections.getSection());
        float completed = (float) sections.getCompletedMiles();
        float total = (float) sections.getTotalMiles();
        float progress = ((completed / total) * 100);
        holder.milesProgressBar.setProgress((int) progress);
        holder.completedMilesText.setText("" + sections.getCompletedMiles() + " miles completed");
        holder.milestonesText.setText(sections.getMilestoneName().substring(0,2));
        holder.imageBackroundCircle.setImageDrawable(getDrawable(sections.getMilestoneId()));
    }

    Drawable getDrawable(int milestoneId) {
        Resources res = context.getResources();
        Drawable mDrawable = res.getDrawable(R.drawable.backround_circle_green);
        ColorFilter filter = new LightingColorFilter( res.getColor(colorsArray[new Random().nextInt(colorsArray.length)]),
                Color.BLACK);
     //   ColorFilterGenerator.from(drawable).to(Color.RED);
        mDrawable.setColorFilter(res.getColor(colorsArray[new Random().nextInt(colorsArray.length)]), PorterDuff.Mode.SRC_ATOP);
        return mDrawable;
    }

    @Override
    public int getItemCount() {
        return sectionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_cls_sections)
        TextView classAndSectionText;
        @BindView(R.id.text_milestone)
        TextView milestonesText;
        @BindView(R.id.image_bg_round)
        ImageView imageBackroundCircle;
        @BindView(R.id.progress_miles)
        CustomProgressBar milesProgressBar;
        @BindView(R.id.text_completed_miles)
        TextView completedMilesText;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}