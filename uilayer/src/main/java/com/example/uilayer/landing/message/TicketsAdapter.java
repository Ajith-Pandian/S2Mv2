package com.example.uilayer.landing.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.domainlayer.models.Ticket;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.manage.ManageTeachersActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IS_TRAINING;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILE_INDEX;
import static com.example.domainlayer.Constants.KEY_NOTE;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;


/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    private Context context;
    private int[] colorsArray = {R.color.mile_oolor1, R.color.mile_oolor2, R.color.mile_oolor3,
            R.color.mile_oolor4, R.color.mile_oolor5, R.color.mile_oolor6};
    private List<Ticket> ticketsList;

    public TicketsAdapter(Context context, List<Ticket> ticketsList) {
        this.ticketsList = ticketsList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Ticket ticket = ticketsList.get(position);
        holder.userName.setText(ticket.getUserName());
        holder.content.setText(ticket.getContent());
        holder.category.setText(ticket.getCategory());
        holder.date.setText(ticket.getDate());
        holder.status.setText(ticket.getStatus());
        holder.ticketId.setText(""+ticket.getNumber());

        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_profile);
        holder.profileImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.profileImage.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(context, bitmap, 20, 0));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (!ticket.getProfileUrl().equals(""))
            Picasso.with(context).load(ticket.getProfileUrl()).placeholder(R.drawable.profile).into(target);
    }


    void openMessagesActivity(int position) {

        final Intent intent = new Intent(context, MessageActivity.class);

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
            openMessagesActivity(position);

        } catch (JSONException ex) {
            Log.e("Save miles", "saveMiles: ", ex);
        }
    }

    @Override
    public int getItemCount() {
        return ticketsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_user_name)
        TextView userName;
        @BindView(R.id.image_user_ticket)
        ImageView profileImage;
        @BindView(R.id.text_category)
        TextView category;
        @BindView(R.id.text_ticket_id)
        TextView ticketId;
        @BindView(R.id.text_content)
        TextView content;
        @BindView(R.id.text_date)
        TextView date;
        @BindView(R.id.text_status)
        AppCompatTextView status;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
