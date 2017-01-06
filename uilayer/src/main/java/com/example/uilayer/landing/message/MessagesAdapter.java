package com.example.uilayer.landing.message;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.domainlayer.models.Message;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by thoughtchimp on 1/2/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int SEND = 0;
    private final int RECEIVE = 1;
    private final int IS_FIRST = 21;
    private Context context;

    private List<Message> messagesList;

    public MessagesAdapter(Context context, List<Message> messagesList) {
        this.messagesList = messagesList;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case IS_FIRST:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_header, parent, false);
                return new MessagesAdapter.FirstItemViewHolder(itemView);

            case SEND:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_send, parent, false);
                return new MessagesAdapter.SendViewHolder(itemView);

            case RECEIVE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_receive, parent, false);
                return new MessagesAdapter.ReceiveViewHolder(itemView);

            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_send, parent, false);
                return new MessagesAdapter.SendViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Message message = messagesList.get(position);
        if (getItemViewType(position) == IS_FIRST) {
            final MessagesAdapter.FirstItemViewHolder viewHolder = (MessagesAdapter.FirstItemViewHolder) holder;
            viewHolder.content.setText(message.getContent());
        } else if (getItemViewType(position) == SEND) {
            final MessagesAdapter.SendViewHolder viewHolder = (MessagesAdapter.SendViewHolder) holder;
            viewHolder.message.setText(message.getContent());
            viewHolder.date.setText(message.getDate());
            viewHolder.time.setText(message.getTime());
        } else {
            final MessagesAdapter.ReceiveViewHolder viewHolder = (MessagesAdapter.ReceiveViewHolder) holder;
            viewHolder.message.setText(message.getContent());
           // viewHolder.senderName.setText("Oponent Name");
            viewHolder.date.setText(message.getDate());
            viewHolder.time.setText(message.getTime());
            Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_profile);
            viewHolder.senderImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));

           /* Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    viewHolder.senderImage.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(context, bitmap, 20, 0));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            if (!message.getSenderImage().equals(""))
                Picasso.with(context).load(message.getSenderImage()).placeholder(R.drawable.profile).into(target);*/
        }
    }


    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return IS_FIRST;
        else if (messagesList.get(position).isSend())
            return SEND;
        else
            return RECEIVE;
    }

    class SendViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_message)
        TextView message;
        @BindView(R.id.text_date)
        TextView date;
        @BindView(R.id.text_time)
        TextView time;

        private SendViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    class ReceiveViewHolder extends RecyclerView.ViewHolder {
  /*      @BindView(R.id.text_user_name)
        TextView senderName;*/
        @BindView(R.id.image_receiver)
        ImageView senderImage;
        @BindView(R.id.text_message)
        TextView message;
        @BindView(R.id.text_date)
        TextView date;
        @BindView(R.id.text_time)
        TextView time;

        private ReceiveViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class FirstItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text)
        TextView content;
        @BindView(R.id.image_quotes)
        ImageView senderImage;

        private FirstItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


