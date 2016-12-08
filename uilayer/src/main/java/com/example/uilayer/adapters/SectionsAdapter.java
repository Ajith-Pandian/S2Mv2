package com.example.uilayer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.CustomProgressBar;
import com.example.uilayer.exceptionHandler.VolleyStringRequest;
import com.example.uilayer.milestones.MilestonesActivity;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.MILES_URL;
import static com.example.domainlayer.Constants.MILES_URL_SUFFIX;
import static com.example.domainlayer.Constants.TRAININGS_URL;
import static com.example.domainlayer.Constants.TRAININGS_URL_SUFFIX;

/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder> {

    int i = 0;
    private Context context;
    private int[] colorsArray = {R.color.fade_red, R.color.fade_orange, R.color.fade_blue,
            R.color.card_color_four, R.color.card_color_five, R.color.card_color_six};
    private List<Sections> sectionDetailsList;

    public SectionsAdapter(Context context, List<Sections> sectionDetailsList) {
        this.sectionDetailsList = sectionDetailsList;
        this.context = context;
    }

    private void showPopupMenu(View view, int position) {
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.PopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, view);
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
        layoutParams.width = (int) ((parent).getMeasuredWidth() - (2 * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics()))) / 3;
        layoutParams.height = (int) (parent.getMeasuredHeight() - (2 * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics()))) / 3;
        itemView.setLayoutParams(layoutParams);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Sections sectionDetails = sectionDetailsList.get(position);
        holder.classname.setText(sectionDetails.get_Class());
        holder.sectionName.setText(sectionDetails.getSection());
        float completed = (float) sectionDetails.getCompletedMiles();
        float total = (float) sectionDetails.getTotalMiles();
        float progress = ((completed / total) * 100);

        holder.completedMiles.setText("" + sectionDetails.getCompletedMiles() + " miles completed");
        holder.progressBar.setProgress((int) progress);
        holder.threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, position);
            }
        });
        //TODO:color coressponding milestone
        holder.backgroundLayout.setBackgroundColor(context.getResources().getColor(colorsArray[new Random().nextInt(colorsArray.length)]));

        holder.rootLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getMilestoneDetails(position);


                    }
                });
    }

    void getMilestoneDetails(final int position) {
        String milesUrl = MILES_URL + sectionDetailsList.get(position).getMilestoneId() + MILES_URL_SUFFIX;
        VolleyStringRequest milesRequest = new VolleyStringRequest(Request.Method.GET, milesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Miles", "onResponse: " + response);
                        openMilestonesActivity(position);
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
        });

        String trainingsUrl = TRAININGS_URL + sectionDetailsList.get(position).getMilestoneId() + TRAININGS_URL_SUFFIX;
        VolleyStringRequest trainingsRequest = new VolleyStringRequest(Request.Method.GET, trainingsUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Miles", "onResponse: " + response);
                        openMilestonesActivity(position);
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
        });

        VolleySingleton.getInstance(context).addToRequestQueue(milesRequest);
        VolleySingleton.getInstance(context).addToRequestQueue(trainingsRequest);
    }

    void openMilestonesActivity(int position) {
        if (i == 1) {
            final Intent intent = new Intent(context, MilestonesActivity.class);
            intent.putExtra("class_name", sectionDetailsList.get(position).get_Class());
            intent.putExtra("section_name", sectionDetailsList.get(position).getSection());
            context.startActivity(intent);
        } else
            i++;
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
        @BindView(R.id.bottom_layout)
        RelativeLayout bottomLayout;
        @BindView(R.id.card_school_update_root)
        LinearLayout rootLayout;

        @BindView(R.id.dots)
        TextView threeDots;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
