package com.example.uilayer.milestones.betterAdapter.viewmodel;

import com.example.uilayer.milestones.betterAdapter.TypeFactory;
import com.example.uilayer.milestones.betterAdapter.model.Training;

/**
 * Created by thoughtchimp on 11/24/2016.
 */

public class TrainingViewModel extends ViewModel {
    private Training training;

    public TrainingViewModel(Training training) {
        this.training = training;
    }

    public int type(TypeFactory typeFactory) {
        return typeFactory.type(training);
    }
}

