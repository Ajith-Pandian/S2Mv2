package com.example.uilayer.manage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.network.VolleySingleton;
import com.example.domainlayer.temp.DataParser;
import com.example.domainlayer.utils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.milestones.MilesActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.example.domainlayer.Constants.FEEDBACK_UNDO_URL;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILE_ID;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SECTION_ID;
import static com.example.domainlayer.Constants.MILES_TRAININGS_URL;
import static com.example.domainlayer.Constants.TEMP_ACCESS_TOKEN;

/**
 * Created by thoughtchimp on 12/23/2016.
 */

public class TeachersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> teachersList;
    private List<Sections> sectionsList;
    private Context context;
    private int undoId;

    public TeachersAdapter(Context context, List<User> teachersList) {
        this.teachersList = teachersList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_teachers, parent, false);
        return new TeachersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final TeachersViewHolder viewHolder = (TeachersViewHolder) holder;
        final User user = teachersList.get(position);
        viewHolder.name.setText(user.getName());
        viewHolder.phoneNum.setText(user.getPhoneNum());

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                viewHolder.profileImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(context).load(user.getAvatar()).into(target);

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
        return teachersList.size();
    }


    class TeachersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_teacher_name)
        TextView name;
        @BindView(R.id.image_profile_teacher)
        ImageView profileImage;
        @BindView(R.id.text_teacher_phone_num)
        TextView phoneNum;


        TeachersViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
