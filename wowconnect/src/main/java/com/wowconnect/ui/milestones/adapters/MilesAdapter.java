package com.wowconnect.ui.milestones.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wowconnect.NewDataHolder;
import com.wowconnect.R;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.mcq.MCQs;
import com.wowconnect.models.milestones.TMileData;
import com.wowconnect.models.milestones.TMiles;
import com.wowconnect.ui.customUtils.VolleyStringRequest;
import com.wowconnect.ui.milestones.MilesActivity;
import com.wowconnect.ui.milestones.UndoDoneListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

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
                        ArrayList<TMileData> miledataList;
                        if (isArchive)
                            miledataList = holder.getArchiveList().get(position).getMileData();
                        else
                            miledataList = holder.getMilesList().get(position).getMileData();
                        holder.setMilesDataList(miledataList);
                        holder.setCurrentMileId(mile.getId());
                        openActivity(MilesActivity.class, isIntro, true, mile.isCompletable(), milestonesList.get(position).getId());
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

                viewHolder.type.setVisibility(isArchive ? View.GONE : View.VISIBLE);

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
                        ArrayList<MCQs> trainingMCQsList;
                        if (isIntro) {
                            mileDataArrayList = holder.getIntroTrainingsList().get(position).getMileData();
                            trainingMCQsList = holder.getIntroTrainingsList().get(position).getMcqs();
                        } else {
                            mileDataArrayList = holder.getMilesList().get(position).getMileData();
                            trainingMCQsList = holder.getMilesList().get(position).getMcqs();
                        }
                        holder.setMilesDataList(mileDataArrayList);
                        holder.setCurrentMileMcqs(trainingMCQsList);
                        holder.setCurrentMileId(training.getId());
                        openActivity(MilesActivity.class, isIntro, false, training.isCompletable(), milestonesList.get(position).getId());
                    }
                });
                tViewHolder.type.setVisibility(isArchive ? View.GONE : View.VISIBLE);
                break;
        }

    }

    private void undoMile(int mileId) {
        undoRequest = new VolleyStringRequest(Request.Method.POST,
                Constants.SCHOOLS_URL + SharedPreferenceHelper.getSchoolId() + Constants.SEPERATOR +
                        Constants.KEY_SECTIONS + Constants.SEPERATOR + NewDataHolder.getInstance(context).getCurrentSectionId() + Constants.SEPERATOR +
                        Constants.KEY_CONTENT + Constants.SEPERATOR + mileId + Constants.SEPERATOR + Constants.KEY_UNDO,
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


    private void openActivity(Class<?> activityClass, boolean isIntro, boolean isMile, boolean isCompletable, int id) {
        Intent intent = new Intent(context, MilesActivity.class);
        intent.putExtra("isMile", isMile);
        intent.putExtra(Constants.KEY_ID, id);
        intent.putExtra("isCompletable", isCompletable);
        intent.putExtra("isIntro", isIntro);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return (milestonesList != null && milestonesList.size() > 0) ? milestonesList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return milestonesList.get(position).getType().equals(Constants.TYPE_TRAINING) ? 1 : 0;
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
