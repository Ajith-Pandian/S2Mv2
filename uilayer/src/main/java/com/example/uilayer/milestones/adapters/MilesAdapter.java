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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.R;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.milestones.MilesActivity;
import com.example.uilayer.milestones.UndoDoneListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.domainlayer.Constants.FEEDBACK_UNDO_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_CONTENT;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILE_ID;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_SECTION_ID;
import static com.example.domainlayer.Constants.KEY_UNDO;
import static com.example.domainlayer.Constants.SCHOOLS_URL;
import static com.example.domainlayer.Constants.SEPERATOR;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TYPE_TRAINING;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class MilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    VolleyStringRequest undoRequest;
    private List<TMiles> milestonesList;
    private Context context;
    private int undoId;
    private boolean isIntro, isArchive;
    private UndoDoneListener listener;

    public MilesAdapter(Context context, List<TMiles> milestonesList, boolean isArchive, boolean isIntro, UndoDoneListener listener) {
        this.milestonesList = milestonesList;
        this.listener = listener;
        this.context = context;
        this.isArchive = isArchive;
        this.isIntro = isIntro;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {

            case 0:
                MilesViewHolder viewHolder = (MilesViewHolder) holder;
                final TMiles mile = milestonesList.get(position);
                viewHolder.title.setText(mile.getTitle());
                viewHolder.textContent.setText(mile.getNote());
                viewHolder.textPosition.setText("" + mile.getMileIndex());
                viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NewDataHolder holder = NewDataHolder.getInstance(context);
                        holder.setCurrentMileTitle(mile.getTitle());
                        holder.setMilesDataList(holder.getMilesList().get(position).getMileData());
                        holder.setCurrentMileId(mile.getId());
                        openActivity(MilesActivity.class, true, mile.isCompletable(), milestonesList.get(position).getId());
                    }
                });

                if (isArchive && position == 0) {
                    viewHolder.undoButton.setVisibility(View.VISIBLE);
                    viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            undoMile(mile.getId());
                        }
                    });
                } else
                    viewHolder.undoButton.setVisibility(View.GONE);


                break;
            case 1:
                TrainingsViewHolder tViewHolder = (TrainingsViewHolder) holder;
                final TMiles training = milestonesList.get(position);
                tViewHolder.title.setText(training.getTitle());
                tViewHolder.textContent.setText(training.getNote());
                tViewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NewDataHolder holder = NewDataHolder.getInstance(context);
                        holder.setCurrentMileTitle(training.getTitle());
                        ArrayList<TMileData> mileDataArrayList;
                        if (isIntro)
                            mileDataArrayList = holder.getIntroTrainingsList().get(position).getMileData();
                        else
                            mileDataArrayList = holder.getMilesList().get(position).getMileData();

                        holder.setMilesDataList(mileDataArrayList);
                        holder.setCurrentMileId(training.getId());
                        openActivity(MilesActivity.class, false, training.isCompletable(), milestonesList.get(position).getId());


                    }
                });
                break;
        }

    }

    private void undoMile(int mileId) {
        undoRequest = new VolleyStringRequest(Request.Method.POST,
                SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + SEPERATOR +
                        KEY_SECTIONS + SEPERATOR + NewDataHolder.getInstance(context).getCurrentSectionId() + SEPERATOR +
                        KEY_CONTENT + SEPERATOR + mileId + SEPERATOR + KEY_UNDO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("undoRequest", "onResponse: " + response);
                        if (listener != null) {
                            listener.onDone();
                        }
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("undoRequest", "onErrorResponse: " + error);

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

        VolleySingleton.getInstance(context).addToRequestQueue(undoRequest);
    }

    void undoThisMile(final int undoId) {
        VolleyStringRequest milesRequest = new VolleyStringRequest(Request.Method.POST, FEEDBACK_UNDO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UndoFeedBack", "onResponse: " + response);
                        Toast.makeText(context, "undo done", Toast.LENGTH_SHORT).show();

                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("UndoFeedBack", "onErrorResponse: " + error);

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
                params.put(KEY_MILE_ID, String.valueOf(undoId));
                params.put(KEY_MILESTONE_ID, "1");
                params.put(KEY_SECTION_ID, "4");
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

        VolleySingleton.getInstance(context).addToRequestQueue(milesRequest);
    }

    private void openActivity(Class<?> activityClass, boolean isMile, boolean isCompletable, int id) {
        Intent intent = new Intent(context, MilesActivity.class);
        intent.putExtra("isMile", isMile);
        intent.putExtra(KEY_ID, id);
        intent.putExtra("isCompletable", isCompletable);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return (milestonesList != null && milestonesList.size() > 0) ? milestonesList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return milestonesList.get(position).getType().equals(TYPE_TRAINING) ? 1 : 0;
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
