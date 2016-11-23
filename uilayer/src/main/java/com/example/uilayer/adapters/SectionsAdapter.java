package com.example.uilayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.CustomProgressBar;
import com.example.uilayer.models.SectionDetails;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder> {

    private List<SectionDetails> sectionDetailsList;
Context context;
    public SectionsAdapter(Context context,List<SectionDetails> sectionDetailsList) {
        this.sectionDetailsList = sectionDetailsList;
        this.context=context;
    }
int[] colorsArray={R.color.fade_red,R.color.fade_orange,R.color.fade_blue};
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section_details, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = (int) (parent.getMeasuredWidth() / (3));
         layoutParams.height = (int) (parent.getMeasuredHeight() / (3));
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SectionDetails sectionDetails = sectionDetailsList.get(position);
        holder.classname.setText(sectionDetails.getClassName());
        holder.sectionName.setText(sectionDetails.getSectionName());
        holder.completedMiles.setText("" + sectionDetails.getCompletedMiles() + " miles completed");
        holder.progressBar.setProgress(sectionDetails.getCompletedMiles());
        //TODO:color coressponding milestone
        holder.backgroundLayout.setBackgroundColor(context.getResources().getColor(colorsArray[new Random().nextInt(colorsArray.length)]));
    }


    @Override
    public int getItemCount() {
        return sectionDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_class_name)
        TextView classname;
        @BindView(R.id.text_section_name)
        TextView sectionName;
        @BindView(R.id.text_completed_milestones)
        TextView completedMiles;

        @BindView(R.id.progress_miles)
        CustomProgressBar progressBar;
        @BindView(R.id.sections_layout)
        RelativeLayout backgroundLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
