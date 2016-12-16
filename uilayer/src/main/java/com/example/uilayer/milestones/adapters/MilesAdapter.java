package com.example.uilayer.milestones.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataParser;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.milestones.MilesActivity;
import com.example.uilayer.milestones.TrainingActivity;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.MILES_TRAININGS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class MilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TMiles> milestonesList;
    private Context context;
    private int undoId;

    public MilesAdapter(Context context, List<TMiles> milestonesList, int undoId) {
        this.milestonesList = milestonesList;
        this.context = context;
        this.undoId = undoId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_miles, parent, false);
                return new MilesViewHolder(itemView);
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_trainings, parent, false);
                return new TrainingsViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_miles, parent, false);
                return new MilesViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {

            case 0:
                MilesViewHolder viewHolder = (MilesViewHolder) holder;
                TMiles mile = milestonesList.get(position);
                viewHolder.title.setText(mile.getTitle());
                viewHolder.textContent.setText(mile.getNote());
                viewHolder.textPosition.setText("" + mile.getMileIndex());
                viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDetails(position, true);
                    }
                });
                if (undoId != -1 && undoId == mile.getId())
                    viewHolder.undoButton.setVisibility(View.VISIBLE);
                else
                    viewHolder.undoButton.setVisibility(View.GONE);


                break;
            case 1:
                TrainingsViewHolder tViewHolder = (TrainingsViewHolder) holder;
                TMiles training = milestonesList.get(position);
                tViewHolder.title.setText(training.getTitle());
                tViewHolder.textContent.setText(training.getNote());
                tViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //     openActivity(TrainingActivity.class);
                        getDetails(position, false);

                    }
                });
                break;
        }

    }


    void getDetails(final int position, final boolean isMile) {
        VolleyStringRequest milesRequest = new VolleyStringRequest(Request.Method.GET, MILES_TRAININGS_URL + "/1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MilesDetails", "onResponse: " + response);
                        DataHolder.getInstance(context).setCurrentMileData(new DataParser().getMilesData(response));
                        openActivity(MilesActivity.class, isMile);
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("MilesDetails", "onErrorResponse: " + error);

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
                params.put(KEY_SCHOOL_ID, "2");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, TEMP_ACCESS_TOKEN);
                return header;
            }

        };

        VolleySingleton.getInstance(context).addToRequestQueue(milesRequest);
    }

    private void openActivity(Class<?> activityClass, boolean isMile) {
        Intent intent = new Intent(context, activityClass);
        intent.putExtra("isMile", isMile);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return milestonesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return milestonesList.get(position).getIsTraining();
    }

    class MilesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title_type)
        TextView type;
        @BindView(R.id.text_title)
        TextView title;
        @BindView(R.id.text_content)
        TextView textContent;
        @BindView(R.id.text_big_number)
        TextView textPosition;
        @BindView(R.id.layout_root_miles)
        RelativeLayout rootLayout;
        @BindView(R.id.button_undo)
        Button undoButton;

        MilesViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class TrainingsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title_type)
        TextView type;
        @BindView(R.id.text_title)
        TextView title;
        @BindView(R.id.text_content)
        TextView textContent;
        @BindView(R.id.layout_root_trainings)
        RelativeLayout rootLayout;

        public TrainingsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
