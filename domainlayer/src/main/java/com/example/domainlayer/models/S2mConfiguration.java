package com.example.domainlayer.models;

import java.util.ArrayList;

/**
 * Created by Ajit on 01-02-2017.
 */

public class S2mConfiguration {
    private ArrayList<Schools> schoolsArrayList;
    private ArrayList<Milestones> milestonesArrayList;
    private String
            mileQuestion,
            trainingQuestion,
            mileFeedbackUpQuestion,
            mileFeedbackDownQuestion,
            trainingFeedbackUpQuestion,
            trainingFeedbackDownQuestion;
    private ArrayList<String>
            mileFeedbackUpOptions,
            mileFeedbackDownOptions,
            trainingFeedbackUpOptions,
            trainingFeedbackDownOptions,
            countryCodes,
            colorSchemes;

    public ArrayList<Schools> getSchoolsArrayList() {
        return schoolsArrayList;
    }

    public void setSchoolsArrayList(ArrayList<Schools> schoolsArrayList) {
        this.schoolsArrayList = schoolsArrayList;
    }

    public ArrayList<Milestones> getMilestonesArrayList() {
        return milestonesArrayList;
    }

    public void setMilestonesArrayList(ArrayList<Milestones> milestonesArrayList) {
        this.milestonesArrayList = milestonesArrayList;
    }

    public String getMileFeedbackUpQuestion() {
        return mileFeedbackUpQuestion;
    }

    public void setMileFeedbackUpQuestion(String mileFeedbackUpQuestion) {
        this.mileFeedbackUpQuestion = mileFeedbackUpQuestion;
    }

    public String getMileFeedbackDownQuestion() {
        return mileFeedbackDownQuestion;
    }

    public void setMileFeedbackDownQuestion(String mileFeedbackDownQuestion) {
        this.mileFeedbackDownQuestion = mileFeedbackDownQuestion;
    }

    public String getTrainingFeedbackUpQuestion() {
        return trainingFeedbackUpQuestion;
    }

    public void setTrainingFeedbackUpQuestion(String trainingFeedbackUpQuestion) {
        this.trainingFeedbackUpQuestion = trainingFeedbackUpQuestion;
    }

    public String getTrainingFeedbackDownQuestion() {
        return trainingFeedbackDownQuestion;
    }

    public void setTrainingFeedbackDownQuestion(String trainingFeedbackDownQuestion) {
        this.trainingFeedbackDownQuestion = trainingFeedbackDownQuestion;
    }

    public ArrayList<String> getMileFeedbackUpOptions() {
        return mileFeedbackUpOptions;
    }

    public void setMileFeedbackUpOptions(ArrayList<String> mileFeedbackUpOptions) {
        this.mileFeedbackUpOptions = mileFeedbackUpOptions;
    }

    public ArrayList<String> getMileFeedbackDownOptions() {
        return mileFeedbackDownOptions;
    }

    public void setMileFeedbackDownOptions(ArrayList<String> mileFeedbackDownOptions) {
        this.mileFeedbackDownOptions = mileFeedbackDownOptions;
    }

    public ArrayList<String> getTrainingFeedbackUpOptions() {
        return trainingFeedbackUpOptions;
    }

    public void setTrainingFeedbackUpOptions(ArrayList<String> trainingFeedbackUpOptions) {
        this.trainingFeedbackUpOptions = trainingFeedbackUpOptions;
    }

    public ArrayList<String> getTrainingFeedbackDownOptions() {
        return trainingFeedbackDownOptions;
    }

    public void setTrainingFeedbackDownOptions(ArrayList<String> trainingFeedbackDownOptions) {
        this.trainingFeedbackDownOptions = trainingFeedbackDownOptions;
    }

    public ArrayList<String> getCountryCodes() {
        return countryCodes;
    }

    public void setCountryCodes(ArrayList<String> countryCodes) {
        this.countryCodes = countryCodes;
    }

    public ArrayList<String> getColorSchemes() {
        return colorSchemes;
    }

    public void setColorSchemes(ArrayList<String> colorSchemes) {
        this.colorSchemes = colorSchemes;
    }

    public String getMileQuestion() {
        return mileQuestion;
    }

    public void setMileQuestion(String mileQuestion) {
        this.mileQuestion = mileQuestion;
    }

    public String getTrainingQuestion() {
        return trainingQuestion;
    }

    public void setTrainingQuestion(String trainingQuestion) {
        this.trainingQuestion = trainingQuestion;
    }
}
