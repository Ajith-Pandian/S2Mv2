package com.example.uilayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.S2MApplication;
import com.example.uilayer.customUtils.CustomProgressBar;
import com.example.uilayer.manage.ManageTeachersActivity;
import com.example.uilayer.milestones.MilestonesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IS_TRAINING;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILE_INDEX;
import static com.example.domainlayer.Constants.KEY_NOTE;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_SECTION_ID;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.MILES_TRAININGS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;


/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder> {
    private ManageTeachersActivity.TeachersSectionsFragment.TeacherListener listener;
    private int rowsCount;
    private Context context;
    private int[] colorsArray = {R.color.mile_oolor1, R.color.mile_oolor2, R.color.mile_oolor3,
            R.color.mile_oolor4, R.color.mile_oolor5, R.color.mile_oolor6};
    private List<Sections> sectionDetailsList;
    private boolean editable;

    public SectionsAdapter(Context context, List<Sections> sectionDetailsList,
                           int rowsCount,
                           ManageTeachersActivity.TeachersSectionsFragment.TeacherListener listener, boolean editable) {
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
        layoutParams.width = ((parent).getMeasuredWidth() - (2 * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics()))) / 3;
/*        layoutParams.height = (parent.getMeasuredHeight() -
                ((rowsCount + 2) * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics()))) / rowsCount;*/
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Sections sectionDetails = sectionDetailsList.get(holder.getAdapterPosition());
        holder.classname.setText(sectionDetails.get_Class());
        holder.classname.setAlpha(0.8f);
        holder.sectionName.setText(sectionDetails.getSection());
        holder.mileStoneName.setText(sectionDetails.getMilestoneName());
        float completed = (float) sectionDetails.getCompletedMiles();
        float total = (float) sectionDetails.getTotalMiles();
        float progress = ((completed / total) * 100);

        holder.completedMiles.setText("" + sectionDetails.getCompletedMiles() + " miles completed");
        holder.progressBar.setProgress((int) progress);

        //TODO:color coressponding milestone
        holder.backgroundLayout.setBackgroundColor(context.getResources().getColor(colorsArray[new Random().nextInt(colorsArray.length)]));

        holder.rootLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  getMilestoneDetails(position);
                        getOrderedMilestoneDetails(holder.getAdapterPosition());
                    }
                });

 /*       if (!com.example.domainlayer.temp.DataHolder.getInstance(context).getUser().getType().equals(TYPE_TEACHER)) {
            holder.threeDots.setVisibility(View.VISIBLE);
        } else holder.threeDots.setVisibility(View.GONE);*/
        if (editable) {
            holder.dotsLayout.setVisibility(View.VISIBLE);
            holder.dotsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null)
                        showPopupMenu(view, holder.getAdapterPosition());
                }
            });
        } else holder.dotsLayout.setVisibility(View.GONE);
    }

    private void getOrderedMilestoneDetails(final int position) {
        VolleyStringRequest milesRequest = new VolleyStringRequest(Request.Method.GET,
                Constants.BASE_URL
                        + KEY_SECTION
                        + "/"+String.valueOf(sectionDetailsList.get(position).getId())
                        + Constants.MILE_TRAININGS_SUFFIX,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Miles", "onResponse: " + response);
                        saveMiles(position, response);

                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("Miles", "onErrorResponse: " + error);

                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();
                // params.put(KEY_SECTION_ID, sectionDetailsList.get(position).getId());
                params.put(KEY_SECTION_ID, String.valueOf(sectionDetailsList.get(position).getId()));
                // params.put(KEY_SCHOOL_ID,  sectionDetailsList.get(position).getSchoolId());
                params.put(KEY_SCHOOL_ID, "2");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                return header;
            }
        };

        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(milesRequest);
    }

    private void openMilestonesActivity(int position) {
        String _class = sectionDetailsList.get(position).get_Class();
        String section = sectionDetailsList.get(position).getSection();
        String title = sectionDetailsList.get(position).getMilestoneName();
        final Intent intent = new Intent(context, MilestonesActivity.class);
        DataHolder.getInstance(context).setCurrentClass(_class);
        DataHolder.getInstance(context).setCurrentSection(section);
        DataHolder.getInstance(context).setCurrentMileTitle(title);
        intent.putExtra("class_name", _class);
        intent.putExtra("section_name", section);
        context.startActivity(intent);

    }

    private void saveMiles(int position, String milesResponse) {

        TMiles miles = null;
        ArrayList<TMiles> milesList = new ArrayList<>();
        try {

            JSONArray milesArray = new JSONArray(milesResponse);
            for (int j = 0; j < milesArray.length(); j++) {
                JSONObject milesJson = milesArray.getJSONObject(j);
                miles = new TMiles(milesJson.getInt(KEY_ID),
                        milesJson.getInt(KEY_MILESTONE_ID),
                        milesJson.getInt(KEY_MILE_INDEX),
                        milesJson.getInt(KEY_IS_TRAINING),
                        milesJson.getString(KEY_TITLE),
                        milesJson.getString(KEY_NOTE),
                        milesJson.getString(KEY_TYPE)
                );
                milesList.add(j, miles);
            }

            DataHolder.getInstance(context).setMilesList(milesList);
            openMilestonesActivity(position);

        } catch (JSONException ex) {
            Log.e("Save miles", "saveMiles: ", ex);
        }
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

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
