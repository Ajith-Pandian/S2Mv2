package com.example.uilayer.milestones.betterAdapter;

import android.view.View;

import com.example.uilayer.R;
import com.example.uilayer.milestones.betterAdapter.holder.MilestonesViewHolder;
import com.example.uilayer.milestones.betterAdapter.holder.TrainingViewHolder;
import com.example.uilayer.milestones.betterAdapter.holder.ViewHolder;
import com.example.uilayer.milestones.betterAdapter.model.Mile;
import com.example.uilayer.milestones.betterAdapter.model.Training;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

class TypeFactoryImpl implements TypeFactory {
    @Override
    public int type(Mile mile) {
        return R.layout.item_miles;
    }

    @Override
    public int type(Training training) {
        return R.layout.item_trainings;
    }

    @Override
    public ViewHolder holder(int type, View view) {
        switch (type) {
            case R.layout.item_miles:
                return new MilestonesViewHolder(view);
            case R.layout.item_trainings:
                return new TrainingViewHolder(view);
            default:
                throw new RuntimeException("\"Illegal view type\"");
        }
    }
}
