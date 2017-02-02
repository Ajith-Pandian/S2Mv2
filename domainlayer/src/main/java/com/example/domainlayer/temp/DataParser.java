package com.example.domainlayer.temp;

import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.milestones.TMileData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_CLASS_NAME;
import static com.example.domainlayer.Constants.KEY_MILES_COMPLETED_COUNT;
import static com.example.domainlayer.Constants.KEY_MILES_COUNT;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILESTONE_NAME;
import static com.example.domainlayer.Constants.KEY_MILES_COMPLETION_COUNT;
import static com.example.domainlayer.Constants.KEY_SECTION_NAME;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.PREFIX_CLASS;
import static com.example.domainlayer.Constants.PREFIX_SECTION;
import static com.example.domainlayer.Constants.SPACE;
import static com.example.domainlayer.Constants.TYPE_AUDIO;
import static com.example.domainlayer.Constants.TYPE_IMAGE;
import static com.example.domainlayer.Constants.TYPE_VIDEO;

/**
 * Created by thoughtchimp on 12/9/2016.
 */

public class DataParser {


    public ArrayList<Sections> getSectionsListFromJson(JSONArray sectionsArray) {
        ArrayList<Sections> sectionsArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject milesDatObject = sectionsArray.getJSONObject(i);
                Sections section
                        = new Sections(milesDatObject.getInt(KEY_ID),
                        (PREFIX_CLASS + milesDatObject.getString(KEY_CLASS_NAME)),
                        (PREFIX_SECTION + milesDatObject.getString(KEY_SECTION_NAME)),
                        milesDatObject.getInt(KEY_MILES_COMPLETION_COUNT),
                        milesDatObject.getInt(KEY_MILES_COUNT),
                        (Constants.KEY_MILESTONE_PREFIX + SPACE + milesDatObject.getString(KEY_MILESTONE_NAME)),
                        milesDatObject.getInt(KEY_MILESTONE_ID));
                sectionsArrayList.add(section);
            }
        } catch (JSONException ex) {
            Log.e("getSectionsListFromJson", "getSectionsListFromJson: ", ex);
        }

        return sectionsArrayList;
    }

    public ArrayList<TMileData> getMilesData(String milesDataString) {
        ArrayList<TMileData> sectionsArrayList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(milesDataString);
            JSONArray sectionsArray = jsonObject.getJSONArray("contentMeta");
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject milesDatObject = sectionsArray.getJSONObject(i);
                String type = milesDatObject.getString(KEY_TYPE);
                TMileData mileData
                        = new TMileData(milesDatObject.getInt(KEY_ID),
                        milesDatObject.getString(KEY_TITLE), type
                );
                ArrayList<String> urlsList = new ArrayList<>();
                ArrayList<String> imagesList = new ArrayList<>();
                if (type.equals(TYPE_VIDEO) || type.equals(TYPE_AUDIO)) {
                    JSONArray urlsArray = new JSONArray(milesDatObject.getString(KEY_BODY));

                    for (int j = 0; j < urlsArray.length(); j++) {
                        JSONObject data = urlsArray.getJSONObject(j);
                        String url = data.getString("url");
                        String image = data.getString("image");
                        Log.d("URLS", "getMilesData: " + url);

                        urlsList.add(url);
                        imagesList.add(image);
                    }
                    mileData.setUrlsList(urlsList);
                    mileData.setImagesList(imagesList);
                } else if (type.equals(TYPE_IMAGE)) {
                    JSONArray urlsArray = new JSONArray(milesDatObject.getString(KEY_BODY));

                    for (int j = 0; j < urlsArray.length(); j++) {
                        JSONObject data = urlsArray.getJSONObject(j);
                        String url = data.getString("url");
                        urlsList.add(url);
                    }
                    mileData.setUrlsList(urlsList);
                } else
                    mileData.setBody(milesDatObject.getString(KEY_BODY));
                if (urlsList.size() == 1)
                    mileData.setSingle(true);
                sectionsArrayList.add(mileData);
            }
        } catch (JSONException ex) {

            Log.e("ex", "getMilesData: ", ex);
        }

        return sectionsArrayList;
    }


}
