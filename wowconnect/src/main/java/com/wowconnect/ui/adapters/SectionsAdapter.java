package com.wowconnect.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wowconnect.NetworkHelper;
import com.wowconnect.NewDataHolder;
import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;

import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.models.Sections;
import com.wowconnect.ui.customUtils.views.CustomProgressBar;
import com.wowconnect.ui.manage.TeacherOrSectionListener;
import com.wowconnect.ui.milestones.MilestonesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder> {
    private TeacherOrSectionListener listener;
    private int rowsCount;
    private Context context;
    private int[] colorsArray = {R.color.mile_oolor1, R.color.mile_oolor2, R.color.mile_oolor3,
            R.color.mile_oolor4, R.color.mile_oolor5, R.color.mile_oolor6};
    private List<Sections> sectionDetailsList;
    private boolean editable;

    public SectionsAdapter(Context context, List<Sections> sectionDetailsList,
                           int rowsCount, TeacherOrSectionListener listener, boolean editable) {
        this.sectionDetailsList = sectionDetailsList;
        this.context = context;
        this.rowsCount = rowsCount;
        this.listener = listener;
        this.editable = editable;
    }

    private void showPopupMenu(View view, int position) {
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.PopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_sections, popup.getMenu());
        popup.setOnMenuItemClickListener(new SectionMenuClickListener(position));
        popup.show();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = ((parent).getMeasuredWidth() - (2 * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics()))) / 2;
/*        layoutParams.height = (parent.getMeasuredHeight() -
                ((rowsCount + 2) * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()))) / rowsCount;*/
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Sections sectionDetails = sectionDetailsList.get(holder.getAdapterPosition());
        holder.classname.setText(Constants.PREFIX_CLASS + sectionDetails.get_Class());
        holder.classname.setAlpha(0.8f);
        holder.sectionName.setText(Constants.PREFIX_SECTION + sectionDetails.getSection());
        holder.mileStoneName.setText(sectionDetails.getMilestoneName());
        float completed = (float) sectionDetails.getCompletedMiles();
        float total = (float) sectionDetails.getTotalMiles();
        float progress = ((completed / total) * 100);

        holder.completedMiles.setText("" + sectionDetails.getCompletedMiles() + " miles completed");
        holder.progressBar.setProgress((int) progress);

        //TODO:color coressponding milestone
        holder.backgroundLayout.setBackgroundColor(context.getResources().getColor(colorsArray[new Random().nextInt(colorsArray.length)]));
        Boolean canEdit, canSeeOwn;
        ArrayList<String> userRoles = new NewDataParser().getUserRoles(context,SharedPreferenceHelper.getUserId());
        if (new DataBaseUtil(context).getUser(SharedPreferenceHelper.getUserId()).getType().equals(Constants.USER_TYPE_S2M_ADMIN)) {
            canEdit = true;
            canSeeOwn = false;
        } else if (userRoles.contains(Constants.ROLE_COORDINATOR)) {
            canEdit = true;
            canSeeOwn = false;
        } else if (userRoles.contains(Constants.ROLE_SCL_ADMIN) && userRoles.contains(Constants.ROLE_TEACHER)) {
            canEdit = true;
            canSeeOwn = true;
        } else if (userRoles.contains(Constants.ROLE_SCL_ADMIN)) {
            canEdit = true;
            canSeeOwn = false;
        } else {
            canEdit = false;
            canSeeOwn = false;
        }

        if (canEdit) {
            holder.threeDots.setVisibility(View.VISIBLE);
            holder.dotsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        showPopupMenu(view, holder.getAdapterPosition());
                }
            });
        } else {
            holder.threeDots.setVisibility(View.GONE);
            holder.rootLayout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getMilestoneDetails(holder.getAdapterPosition());
                        }
                    });
        }
        if (canSeeOwn && sectionDetails.getTeacherId() == new DataBaseUtil(context).getUser(SharedPreferenceHelper.getUserId()).getId()) {
            holder.ownBadge.setVisibility(View.VISIBLE);
            holder.rootLayout.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getMilestoneDetails(holder.getAdapterPosition());
                        }
                    });
        } else
            holder.ownBadge.setVisibility(View.GONE);

    }


    private void getMilestoneDetails(final int position) {
        new NetworkHelper(context).getMilestoneContent(sectionDetailsList.get(position).getId(),
                new NetworkHelper.NetworkListener() {
                    @Override
                    public void onFinish() {
                        /*if (NewDataHolder.getInstance(context).getMilesList() != null
                                && NewDataHolder.getInstance(context).getMilesList().size() > 0) {*/
                        openMilestonesActivity(position);
                       /* } else
                            Utils.getInstance().showToast("No Content");*/
                    }
                });
    }

    private void openMilestonesActivity(int position) {
        String _class = sectionDetailsList.get(position).get_Class();
        String section = sectionDetailsList.get(position).getSection();
        String title = sectionDetailsList.get(position).getMilestoneName();
        int milestoneId = sectionDetailsList.get(position).getMilestoneId();
        int sectionId = sectionDetailsList.get(position).getId();
        final Intent intent = new Intent(context, MilestonesActivity.class);
        NewDataHolder.getInstance(context).setCurrentSectionId(sectionId);
        NewDataHolder.getInstance(context).setCurrentMilestoneId(milestoneId);

        NewDataHolder.getInstance(context).setCurrentClassName(Constants.PREFIX_CLASS + _class);
        NewDataHolder.getInstance(context).setCurrentSectionName(Constants.PREFIX_SECTION + section);

        intent.putExtra("class_name", Constants.PREFIX_CLASS + _class);
        intent.putExtra("section_name", Constants.PREFIX_CLASS + section);
        intent.putExtra("is_intro", false);
        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return sectionDetailsList.size();
    }

    private class SectionMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        SectionMenuClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_edit:
                    listener.onEditOptionSelected(false, position);
                    return true;
                case R.id.menu_delete:
                    listener.onDeleteOptionSelected(false, position);
                    return true;
                default:
            }
            return false;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_class_name)
        TextView classname;
        @BindView(R.id.text_section_name)
        TextView sectionName;
        @BindView(R.id.text_milestones)
        TextView mileStoneName;
        @BindView(R.id.text_completed_milestones)
        TextView completedMiles;
        @BindView(R.id.progress_miles)
        CustomProgressBar progressBar;
        @BindView(R.id.sections_layout)
        RelativeLayout backgroundLayout;
        @BindView(R.id.bottom_layout)
        RelativeLayout bottomLayout;
        @BindView(R.id.card_school_update_root)
        LinearLayout rootLayout;
        @BindView(R.id.layout_dots)
        LinearLayout dotsLayout;
        @BindView(R.id.dots)
        TextView threeDots;
        @BindView(R.id.badge_own)
        ImageView ownBadge;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
