package com.example.uilayer.milestones.betterAdapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public abstract class ViewHolder<T> extends RecyclerView.ViewHolder{
    ViewHolder(View view) {
        super(view);
    }

  public   abstract  void bind(T item);
}
