package com.example.domainlayer.temp;

import android.util.Log;

import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.milestones.TMileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_CLASS;
import static com.example.domainlayer.Constants.KEY_COMPLETED_MILESTONES;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILESTONE_NAME;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TOTAL_MILESTONES;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.TYPE_TEXT;

/**
 * Created by thoughtchimp on 12/9/2016.
 */

public class DataParser {


    ArrayList<Sections> getSectionsListFromJson(JSONArray sectionsArray) {
        ArrayList<Sections> sectionsArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject milesDatObject = sectionsArray.getJSONObject(i);
                Sections section
                        = new Sections(milesDatObject.getInt(KEY_ID),
                        milesDatObject.getString(KEY_CLASS),
                        milesDatObject.getString(KEY_SECTION),
                        milesDatObject.getInt(KEY_COMPLETED_MILESTONES),
                        milesDatObject.getInt(KEY_TOTAL_MILESTONES),
                        milesDatObject.getInt(KEY_SCHOOL_ID),
                        milesDatObject.getString(KEY_MILESTONE_NAME),
                        milesDatObject.getInt(KEY_MILESTONE_ID));
                sectionsArrayList.add(section);
            }
        } catch (JSONException ex) {
        }

        return sectionsArrayList;
    }

    public ArrayList<TMileData> getMilesData(String milesDataString) {
        ArrayList<TMileData> sectionsArrayList = new ArrayList<>();
        try {
            JSONArray sectionsArray = new JSONArray(milesDataString);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject milesDatObject = sectionsArray.getJSONObject(i);
                String type = milesDatObject.getString(KEY_TYPE);
                TMileData mileData
                        = new TMileData(milesDatObject.getInt(KEY_ID),
                        milesDatObject.getString(KEY_TITLE), type
                );
                ArrayList<String> urlsList = new ArrayList<>();
                if (!type.equals(TYPE_TEXT)) {
                    JSONArray urlsArray = new JSONArray(milesDatObject.getString(KEY_BODY));

                    for (int j = 0; j < urlsArray.length(); j++) {
                        String url = urlsArray.getString(j);
                        Log.d("URLS", "getMilesData: " + url);
                        urlsList.add(url);
                    }
                    mileData.setUrlsList(urlsList);
                }
                if (urlsList.size() == 1)
                    mileData.setSingle(true);
                sectionsArrayList.add(mileData);
            }
        } catch (JSONException ex) {

            Log.d("ex", "getMilesData: ", ex);
        }

        return sectionsArrayList;
    }
}