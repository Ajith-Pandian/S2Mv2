package com.example.uilayer.landing.message;

import android.content.Context;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.domainlayer.models.Message;

import com.example.uilayer.R;
import com.example.uilayer.customUtils.Utils;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by thoughtchimp on 1/2/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int SEND = 0;
    private final int RECEIVE = 1;
    private final int IS_FIRST = 21;

    private final String TEXT = "text";
    private final String IMAGE = "image";
    private final String AUDIO = "audio";
    private final String VIDEO = "video";
    private final String PDF = "pdf";


    private Context context;

    private List<Message> messagesList;

    public MessagesAdapter(Context context, List<Message> messagesList) {
        this.messagesList = messagesList;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == IS_FIRST) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_header, parent, false);
            return new FirstItemViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message, parent, false);
            return new MessageViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Message message = messagesList.get(position);
        if (getItemViewType(position) == IS_FIRST) {
            final MessagesAdapter.FirstItemViewHolder viewHolder = (MessagesAdapter.FirstItemViewHolder) holder;
            viewHolder.content.setText(message.getContent());
        } else {
            final MessageViewHolder viewHolder = (MessageViewHolder) holder;
            viewHolder.date.setText(message.getTimestamp().split(" ")[0]);
            viewHolder.time.setText(message.getTimestamp().split(" ")[1]);

            switch (message.getType()) {
                case TEXT:
                    viewHolder.contentFrame.removeAllViewsInLayout();
                    TextView textView = new TextView(viewHolder.contentFrame.getContext());
                    textView.setText(message.getContent());
                    textView.setTextColor(context.getResources().getColor(R.color.text_color2));
                    viewHolder.contentFrame.addView(textView);
                    break;
                case IMAGE:
                    viewHolder.contentFrame.removeAllViewsInLayout();
                    View view = LayoutInflater
                            .from(viewHolder.contentFrame.getContext())
                            .inflate(R.layout.item_msg_image, null);
                    viewHolder.contentFrame.addView(view);
                    break;
                case VIDEO:
                case AUDIO:
                    viewHolder.contentFrame.removeAllViewsInLayout();
                    View mediaView = LayoutInflater
                            .from(viewHolder.contentFrame.getContext())
                            .inflate(R.layout.item_msg_video, null);
                    viewHolder.contentFrame.addView(mediaView);
                    break;
                case PDF:
                    viewHolder.contentFrame.removeAllViewsInLayout();
                    View pdfView = LayoutInflater
                            .from(viewHolder.contentFrame.getContext())
                            .inflate(R.layout.item_msg_pdf, null);
                    viewHolder.contentFrame.addView(pdfView);
                    break;
            }


            if (getItemViewType(position) == SEND) {
                setSenderParams(viewHolder);
            } else if (getItemViewType(position) == RECEIVE) {
                setReceiverParams(viewHolder);
            }


            setTimeStampLayoutParams(viewHolder);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.timeDateLayout.getLayoutParams();

            if ( message.getContent().length() >= viewHolder.receiverName.getText().toString().length()) {
                params.addRule(RelativeLayout.ALIGN_RIGHT, viewHolder.contentFrame.getId());
            } else {
                params.addRule(RelativeLayout.ALIGN_RIGHT, viewHolder.receiverName.getId());
            }
            viewHolder.timeDateLayout.setLayoutParams(params);
        }
    }

    @SuppressWarnings("NewApi")
    private void setTimeStampLayoutParams(MessageViewHolder viewHolder) {

    }


    private void setReceiverParams(MessageViewHolder viewHolder) {


        ViewCompat.setBackgroundTintList(viewHolder.messageLayout,
                ColorStateList.valueOf(context.getResources().getColor(R.color.text_input_layer_color1)));

        viewHolder.receiverImage.setVisibility(View.VISIBLE);
        viewHolder.receiverName.setVisibility(View.VISIBLE);
        Bitmap placeHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ph_profile);
        viewHolder.receiverImage.setImageDrawable(Utils.getInstance().getCirclularImage(context, placeHolder));

           /* Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    viewHolder.receiverImage.setImageBitmap(Utils.getInstance().getRoundedCornerBitmap(context, bitmap, 20, 0));
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

    private void setSenderParams(MessageViewHolder viewHolder) {
        ViewCompat.setBackgroundTintList(viewHolder.messageLayout,
                ColorStateList.valueOf(context.getResources().getColor(R.color.green7)));

        viewHolder.receiverImage.setVisibility(View.GONE);
        viewHolder.receiverName.setVisibility(View.GONE);
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


    class MessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_receiver_name)
        TextView receiverName;
        @BindView(R.id.layout_message_content)
        FrameLayout contentFrame;
        @BindView(R.id.image_receiver)
        ImageView receiverImage;
        @BindView(R.id.text_date)
        TextView date;
        @BindView(R.id.text_time)
        TextView time;
        @BindView(R.id.layout_time_date)
        LinearLayout timeDateLayout;
        @BindView(R.id.layout_message)
        RelativeLayout messageLayout;


        private MessageViewHolder(View view) {
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


