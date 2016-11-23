package com.example.uilayer.models;

/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class SectionDetails {
    String className,sectionName;
    int completedMiles;

    public SectionDetails(String className, String sectionName, int completedMiles) {
        this.className = className;
        this.sectionName = sectionName;
        this.completedMiles = completedMiles;
    }

    public String getClassName() {
        return className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getCompletedMiles() {
        return completedMiles;
    }
}
