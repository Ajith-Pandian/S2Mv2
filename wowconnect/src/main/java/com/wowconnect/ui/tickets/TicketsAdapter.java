package com.wowconnect.ui.tickets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wowconnect.domain.Constants;
import com.wowconnect.R;
import com.wowconnect.models.Ticket;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.tickets.message.MessageActivity;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.ViewHolder> {

    private Context context;
    private int[] colorsArray = {R.color.mile_oolor1, R.color.mile_oolor2, R.color.mile_oolor3,
            R.color.mile_oolor5};
    private List<Ticket> ticketsList;

    TicketsAdapter(Context context, List<Ticket> ticketsList) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int exactPosition = holder.getAdapterPosition();
        final Ticket ticket = ticketsList.get(position);
        holder.userName.setText("Receiver Name");
        holder.content.setText(ticket.getSubject());
        holder.category.setText(ticket.getCategory());
        holder.category.setTextColor(context.getResources().getColor(colorsArray[new Random().nextInt(colorsArray.length)]));
        String date = ticket.getCreatedAt().split(" ")[0];
        String time = ticket.getCreatedAt().split(" ")[1];
        holder.date.setText(date);
        final String status = ticket.getStatus();
        holder.ticketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MessageActivity.class)
                        .putExtra("ticketId", ticket.getId())
                        .putExtra("status", status)
                        .putExtra("creatorId",ticket.getCreatorId()));
            }
        });
        if (status.equalsIgnoreCase(Constants.STATUS_OPEN))
            holder.status.setSupportBackgroundTintList(context.getResources().getColorStateList(R.color.red1));
        else if (status.equalsIgnoreCase(Constants.STATUS_CLOSED))
            holder.status.setSupportBackgroundTintList(context.getResources().getColorStateList(R.color.grey1));
        holder.status.setText(ticket.getStatus().toUpperCase());
        holder.ticketId.setText(String.valueOf(ticket.getId()));

        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_profile);
        holder.profileImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));

       /* Target target = new Target() {
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
        if (ticket.getProfileUrl() != null && !ticket.getProfileUrl().equals(""))
            Picasso.with(context).load(ticket.getProfileUrl()).placeholder(R.drawable.profile).into(target);*/
    }


    void openMessagesActivity(int position) {

        final Intent intent = new Intent(context, MessageActivity.class);

        context.startActivity(intent);

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
        @BindView(R.id.layout_ticket)
        LinearLayout ticketLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
