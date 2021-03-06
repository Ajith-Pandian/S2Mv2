package com.wowconnect.ui.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wowconnect.R;
import com.wowconnect.models.Notification;
import com.wowconnect.ui.customUtils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thoughtchimp on 1/20/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notification> notificationsList;
    private Context context;

    public NotificationAdapter(Context context, List<Notification> notificationsList) {
        this.notificationsList = notificationsList;
        this.context = context;
    }

    Notification getItem(int position) {
        return notificationsList.get(position);
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);

        return new NotificationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder holder, int position) {
        Notification notification = notificationsList.get(holder.getAdapterPosition());
        holder.title.setText(notification.getTitle());
        holder.content.setText(notification.getData());
        String readableDate = "";
        try {
            String oldDateFormat = notification.getDate().split(" ")[0];
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(oldDateFormat);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.US);
            sdf.setTimeZone(TimeZone.getDefault());
            readableDate = sdf.format(date);

        } catch (Exception e) {
            readableDate = notification.getDate().split(" ")[0];
        }
        holder.date.setText(readableDate);
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, NetworkProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("com.wowconnect.domain.models.User", getItem(holder.getAdapterPosition()));
                context.startActivity(intent);*/
            }
        });

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.icon.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(context, bitmap, 20, 0));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (notification.getIcon() != null && !notification.getIcon().isEmpty())
            Picasso.with(context).load(notification.getIcon()).placeholder(R.drawable.profile).into(target);
        else {
            Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_profile);
            holder.icon.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));
        }

    }


    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_title)
        TextView title;
        @BindView(R.id.text_date)
        TextView date;
        @BindView(R.id.text_content)
        TextView content;
        @BindView(R.id.image_icon)
        ImageView icon;
        @BindView(R.id.layout_root_notification)
        RelativeLayout rootLayout;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

