package com.example.uilayer.manage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

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
import android.widget.TextView;


import com.example.domainlayer.models.User;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by thoughtchimp on 12/23/2016.
 */

final class TeachersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ManageTeachersActivity.TeachersSectionsFragment.TeacherListener listener;
    private int rowsCount;
    private List<User> teachersList;
    private Context context;

    TeachersAdapter(Context context, List<User> teachersList, int rowsCount, ManageTeachersActivity.TeachersSectionsFragment.TeacherListener teacherListener) {
        this.teachersList = teachersList;
        this.context = context;
        this.rowsCount = rowsCount;
        this.listener = teacherListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_teachers, parent, false);
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = ((parent).getMeasuredWidth() - (2 * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics()))) / 3;
        layoutParams.height = (parent.getMeasuredHeight() -
                ((rowsCount + 2) * (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics()))) / rowsCount;
        itemView.setLayoutParams(layoutParams);
        itemView.setLayoutParams(layoutParams);
        return new TeachersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final TeachersViewHolder viewHolder = (TeachersViewHolder) holder;
        final User user = teachersList.get(position);
        viewHolder.name.setText(user.getFirstName() + " " + user.getLastName());
        viewHolder.phoneNum.setText(user.getPhoneNum());
        viewHolder.threeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view, position);
            }
        });
        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_profile);
        viewHolder.profileImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));
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
        if (!user.getAvatar().equals(""))
            Picasso.with(context).load(user.getAvatar()).into(target);

    }

    private void showPopupMenu(View view, int position) {
        Context wrapper = new ContextThemeWrapper(view.getContext(), R.style.PopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_card, popup.getMenu());
        popup.setOnMenuItemClickListener(new TeacherMenuClickListener(position));
        popup.show();
    }

    @Override
    public int getItemCount() {
        return teachersList.size();
    }
/*    void getDetails(final int position, final boolean isMile) {
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
    }*/

    public class TeacherMenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;

        public TeacherMenuClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_edit:
                    listener.onEditOptionSelected(true,position);
                    return true;
                case R.id.menu_delete:
                    listener.onDeleteOptionSelected(true);
                    return true;
                default:
            }
            return false;
        }
    }

    final class TeachersViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_teacher_name)
        TextView name;
        @BindView(R.id.image_profile_teacher)
        ImageView profileImage;
        @BindView(R.id.dots)
        ImageView threeDots;
        @BindView(R.id.text_teacher_phone_num)
        TextView phoneNum;


        TeachersViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
