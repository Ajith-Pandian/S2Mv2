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
import static com.example.domainlayer.Constants.KEY_STUDENT_COUNT;
import static com.example.domainlayer.Constants.KEY_TEACHER_ID;
import static com.example.domainlayer.Constants.KEY_TEACHER_NAME;
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


    public ArrayList<Sections> getSectionsListFromJson(JSONArray sectionsArray, boolean isUserSections) {
        ArrayList<Sections> sectionsArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionJsonObject = sectionsArray.getJSONObject(i);
                Sections section
                        = new Sections(sectionJsonObject.getInt(KEY_ID),
                        (sectionJsonObject.getString(KEY_CLASS_NAME)),
                        (sectionJsonObject.getString(KEY_SECTION_NAME)),
                        getIntFromJson(sectionJsonObject, KEY_MILES_COMPLETION_COUNT),
                        getIntFromJson(sectionJsonObject, KEY_MILES_COUNT),
                        (sectionJsonObject.getString(KEY_MILESTONE_NAME)),
                        sectionJsonObject.getInt(KEY_MILESTONE_ID),
                        sectionJsonObject.getInt(KEY_STUDENT_COUNT));

                if (sectionJsonObject.has(KEY_TEACHER_NAME) && !sectionJsonObject.isNull(KEY_TEACHER_NAME))
                    section.setTeacherName(sectionJsonObject.getString(KEY_TEACHER_NAME));
                if (sectionJsonObject.has(KEY_TEACHER_ID) && !sectionJsonObject.isNull(KEY_TEACHER_ID))
                    section.setTeacherId(sectionJsonObject.getInt(KEY_TEACHER_ID));

                sectionsArrayList.add(section);
            }
        } catch (JSONException ex) {
            Log.e("getSectionsListFromJson", "getSectionsListFromJson: ", ex);
        }

        return sectionsArrayList;
    }

    int getIntFromJson(JSONObject jsonObject, String key) throws JSONException {
        if (!jsonObject.isNull(KEY_TEACHER_ID))
            return jsonObject.getInt(key);
        else
            return 0;
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
