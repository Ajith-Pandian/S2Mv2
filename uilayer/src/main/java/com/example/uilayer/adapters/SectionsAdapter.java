package com.example.uilayer.adapters;

import android.content.Context;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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

    private Context context;
    private int[] colorsArray = {R.color.fade_red, R.color.fade_orange, R.color.fade_blue,
            R.color.card_color_four, R.color.card_color_five, R.color.card_color_six};
    private List<SectionDetails> sectionDetailsList;

    public SectionsAdapter(Context context, List<SectionDetails> sectionDetailsList) {
        this.sectionDetailsList = sectionDetailsList;
        this.context = context;
    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.PopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, view);
        //  PopupMenu popup = new PopupMenu(context,view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new CardMenuClickListener(position));
        popup.show();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section_details, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = (int) ((parent).getMeasuredWidth()-(2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics()))) / 3;
        layoutParams.height = (int) (parent.getMeasuredHeight()-(2*(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics()))) / 3;
       // layoutParams.width=300;
        //layoutParams.height=300;
       itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SectionDetails sectionDetails = sectionDetailsList.get(position);
        holder.classname.setText(sectionDetails.getClassName());
        holder.sectionName.setText(sectionDetails.getSectionName());
        holder.completedMiles.setText("" + sectionDetails.getCompletedMiles() + " miles completed");
        holder.progressBar.setProgress(sectionDetails.getCompletedMiles());
        holder.threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, position);
            }
        });
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
        @BindView(R.id.dots)
        TextView threeDots;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
