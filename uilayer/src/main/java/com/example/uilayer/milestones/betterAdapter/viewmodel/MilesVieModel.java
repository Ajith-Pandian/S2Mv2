package com.example.uilayer.milestones.betterAdapter.viewmodel;

import com.example.uilayer.milestones.betterAdapter.TypeFactory;
import com.example.uilayer.milestones.betterAdapter.model.Mile;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class MilesVieModel extends ViewModel {
    private Mile mile;

    public MilesVieModel(Mile mile) {
        this.mile = mile;
    }

    public int type(TypeFactory typeFactory) {
        return typeFactory.type(mile);
    }
}
