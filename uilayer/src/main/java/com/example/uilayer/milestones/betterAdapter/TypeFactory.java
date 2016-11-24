package com.example.uilayer.milestones.betterAdapter;

import android.view.View;

import com.example.uilayer.milestones.betterAdapter.holder.ViewHolder;
import com.example.uilayer.milestones.betterAdapter.model.Mile;
import com.example.uilayer.milestones.betterAdapter.model.Training;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public interface TypeFactory {
    int type(Mile mile);
    int type(Training training);
    ViewHolder holder(int type, View view);
}
