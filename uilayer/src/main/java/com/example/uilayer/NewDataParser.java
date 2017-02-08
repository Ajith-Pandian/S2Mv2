package com.example.uilayer;

import android.util.Log;

import com.example.domainlayer.models.Milestones;
import com.example.domainlayer.models.S2mConfiguration;
import com.example.domainlayer.models.Schools;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.uilayer.customUtils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.domainlayer.Constants.KEY_AUDIO_POSTER;
import static com.example.domainlayer.Constants.KEY_AUDIO_URL;
import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_COLOR_SCHEME;
import static com.example.domainlayer.Constants.KEY_CONTENT_DATA;
import static com.example.domainlayer.Constants.KEY_CONTENT_INDEX;
import static com.example.domainlayer.Constants.KEY_COUNTRY_CODES;
import static com.example.domainlayer.Constants.KEY_DESCRIPTION;
import static com.example.domainlayer.Constants.KEY_FEEDBACK_QUESTIONS;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IS_COMPLETED;
import static com.example.domainlayer.Constants.KEY_MILE;
import static com.example.domainlayer.Constants.KEY_MILES;
import static com.example.domainlayer.Constants.KEY_MILESTONE;
import static com.example.domainlayer.Constants.KEY_MILESTONES;
import static com.example.domainlayer.Constants.KEY_NAME;
import static com.example.domainlayer.Constants.KEY_OPTIONS;
import static com.example.domainlayer.Constants.KEY_QUESTION;
import static com.example.domainlayer.Constants.KEY_SCHOOLS;
import static com.example.domainlayer.Constants.KEY_THUMBS_DOWN;
import static com.example.domainlayer.Constants.KEY_THUMBS_UP;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TRAINING;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_VIDEO_ID;
import static com.example.domainlayer.Constants.KEY_VIDEO_POSTER;
import static com.example.domainlayer.Constants.KEY_VIDEO_POSTER_HD;
import static com.example.domainlayer.Constants.TYPE_AUDIO;
import static com.example.domainlayer.Constants.TYPE_IMAGE;
import static com.example.domainlayer.Constants.TYPE_TEXT;
import static com.example.domainlayer.Constants.TYPE_VIDEO;

/**
 * Created by Ajit on 01-02-2017.
 */

public class NewDataParser {
    private S2mConfiguration configuration;

    public S2mConfiguration getS2mConfiguration() {
        configuration = new S2mConfiguration();
        try {
            JSONObject configObj = new JSONObject(SharedPreferenceHelper.getConfiguration());

            configuration.setSchoolsArrayList(getSchoolsList(configObj.getJSONArray(KEY_SCHOOLS)));

            JSONObject feedbackJson = configObj.getJSONObject(KEY_FEEDBACK_QUESTIONS);

            JSONObject milesJson = feedbackJson.getJSONObject(KEY_MILES);
            getDetails(milesJson, true);

            JSONObject trainingJson = feedbackJson.getJSONObject(KEY_TRAINING);
            getDetails(trainingJson, false);

            configuration.setMilestonesArrayList(getMilestonesList(configObj.getJSONArray(KEY_MILESTONES)));

            configuration.setCountryCodes(getStringsFromArray(configObj.getJSONArray(KEY_COUNTRY_CODES)));

            configuration.setColorSchemes(getStringsFromArray(configObj.getJSONArray(KEY_COLOR_SCHEME)));
        } catch (Exception e) {
            Log.d("NewDataParser", "getS2mConfiguration: ");
        }
        return configuration;
    }

    private ArrayList<Schools> getSchoolsList(JSONArray schoolsJsonArray) throws JSONException {
        ArrayList<Schools> schoolsList = new ArrayList<>();
        for (int i = 0; i < schoolsJsonArray.length(); i++) {
            Schools schools = new Schools();
            JSONObject schoolJson = schoolsJsonArray.getJSONObject(i);
            schools.setId(schoolJson.getInt(KEY_ID));
            schools.setName(schoolJson.getString(KEY_NAME));
            schoolsList.add(schools);
        }
        return schoolsList;
    }

    private ArrayList<Milestones> getMilestonesList(JSONArray milestonesJsonArray) throws JSONException {
        ArrayList<Milestones> milestonesArrayList = new ArrayList<>();
        for (int i = 0; i < milestonesJsonArray.length(); i++) {
            JSONObject milestonesJson = milestonesJsonArray.getJSONObject(i);
            milestonesArrayList.add(new Milestones(milestonesJson.getInt(KEY_ID), milestonesJson.getString(KEY_NAME)));
        }
        return milestonesArrayList;
    }

    private void getDetails(JSONObject mileTrainingObj, boolean isMile) {
        try {
            String question = mileTrainingObj.getString(KEY_QUESTION);

            JSONObject thumbsUpObj = mileTrainingObj.getJSONObject(KEY_THUMBS_UP);
            String thumbsUpQuestion = thumbsUpObj.getString(KEY_QUESTION);
            ArrayList<String> thumbsUpOptions = getOptions(thumbsUpObj.getJSONArray(KEY_OPTIONS));

            JSONObject thumbsDownObj = mileTrainingObj.getJSONObject(KEY_THUMBS_DOWN);
            String thumbsDownQuestion = thumbsDownObj.getString(KEY_QUESTION);
            ArrayList<String> thumbsDownOptions = getOptions(thumbsDownObj.getJSONArray(KEY_OPTIONS));
            if (isMile) {
                configuration.setMileQuestion(question);
                configuration.setMileFeedbackUpQuestion(thumbsUpQuestion);
                configuration.setMileFeedbackUpOptions(thumbsUpOptions);
                configuration.setMileFeedbackDownQuestion(thumbsDownQuestion);
                configuration.setMileFeedbackDownOptions(thumbsDownOptions);
            } else {
                configuration.setTrainingQuestion(question);
                configuration.setTrainingFeedbackUpQuestion(thumbsUpQuestion);
                configuration.setTrainingFeedbackUpOptions(thumbsUpOptions);
                configuration.setTrainingFeedbackDownQuestion(thumbsDownQuestion);
                configuration.setTrainingFeedbackDownOptions(thumbsDownOptions);
            }

        } catch (JSONException ex) {
            Log.d("NewDataParser", "getDetails: ");
        }
    }

    private ArrayList<String> getOptions(JSONArray optionsArray) {
        return getStringsFromArray(optionsArray);
    }

    public ArrayList<String> getStringsFromArray(JSONArray stringJsonArray) {
        ArrayList<String> options = new ArrayList<>();
        try {
            for (int i = 0; i < stringJsonArray.length(); i++)
                options.add(i, stringJsonArray.getString(i));
        } catch (JSONException e) {
            Log.d("NewDataParser", "getOptions: ");
        }
        return options;
    }


    public ArrayList<TMiles> getMiles(String milesResponse, boolean isArchive) {
        ArrayList<TMiles> milesList = new ArrayList<>();
        try {
            boolean completable = false;
            boolean isFirstMile = true;
            int archiveIndex = 0;
            JSONArray milesArray = new JSONArray(milesResponse);
            for (int j = 0; j < milesArray.length(); j++) {
                JSONObject milesJson = milesArray.getJSONObject(j);
                TMiles miles = new TMiles();
                String type = milesJson.getString(KEY_TYPE);
                if (type.equals(KEY_MILE)) {
                    if (isFirstMile) {
                        isFirstMile = false;
                        completable = true;
                    } else {
                        completable = false;
                    }
                    if (isArchive) {
                       // archiveIndex++;
                        miles.setMileIndex(++archiveIndex);
                    }
                    if (!milesJson.isNull(KEY_CONTENT_INDEX))
                        miles.setMileIndex(milesJson.getInt(KEY_CONTENT_INDEX));
                } else {
                    if (!milesJson.isNull(KEY_IS_COMPLETED))
                        completable = !milesJson.getBoolean(KEY_IS_COMPLETED);
                }
                Log.d("Completable", "getMiles: " + String.valueOf(completable));
                miles.setId(milesJson.getInt(KEY_ID));
                miles.setType(milesJson.getString(KEY_TYPE));
                miles.setTitle(milesJson.getString(KEY_TITLE));
                miles.setNote(milesJson.getString(KEY_DESCRIPTION));

                miles.setCompletable(completable);


                JSONArray milesDataJsonArray = milesJson.getJSONArray(KEY_CONTENT_DATA);
                ArrayList<TMileData> milesDataList = new ArrayList<>();
                for (int i = 0; i < milesDataJsonArray.length(); i++) {
                    JSONObject dataJson = milesDataJsonArray.getJSONObject(i);
                    String dataType = dataJson.getString(KEY_TYPE);
                    TMileData data = new TMileData(dataJson.getString(KEY_TITLE), dataType);


                    switch (dataType) {
                        case TYPE_TEXT:
                            data.setBody(dataJson.getString(KEY_BODY));
                            break;
                        case TYPE_IMAGE: {
                            ArrayList<String> urlsList = new ArrayList<>();
                            JSONArray imageUrlsArray = new JSONArray(dataJson.getString(KEY_BODY));
                            for (int ii = 0; ii < imageUrlsArray.length(); ii++) {
                                String url = imageUrlsArray.getString(ii);
                                urlsList.add(url);
                            }
                            data.setUrlsList(urlsList);
                            if (urlsList.size() == 1)
                                data.setSingle(true);
                        }
                        break;
                        case TYPE_AUDIO: {
                            ArrayList<String> urlsList = new ArrayList<>();
                            ArrayList<String> imagesList = new ArrayList<>();
                            JSONArray audioUrlsArray = new JSONArray(dataJson.getString(KEY_BODY));
                            for (int ii = 0; ii < audioUrlsArray.length(); ii++) {
                                JSONObject audioData = audioUrlsArray.getJSONObject(ii);
                                String audioUrl = audioData.getString(KEY_AUDIO_URL);
                                String imageUrl = audioData.getString(KEY_AUDIO_POSTER);
                                urlsList.add(ii, audioUrl);
                                imagesList.add(ii, imageUrl);
                            }
                            data.setUrlsList(urlsList);
                            data.setImagesList(imagesList);
                            if (urlsList.size() == 1)
                                data.setSingle(true);
                        }
                        break;
                        case TYPE_VIDEO: {
                            ArrayList<String> idsList = new ArrayList<>();
                            ArrayList<String> imagesList = new ArrayList<>();
                            ArrayList<String> hdImagesList = new ArrayList<>();
                            JSONArray videoUrlsArray = new JSONArray(dataJson.getString(KEY_BODY));
                            for (int ii = 0; ii < videoUrlsArray.length(); ii++) {
                                JSONObject audioData = videoUrlsArray.getJSONObject(ii);
                                String videoIds = audioData.getString(KEY_VIDEO_ID);
                                String imageUrl = audioData.getString(KEY_VIDEO_POSTER);
                                String hdImageUrl = audioData.getString(KEY_VIDEO_POSTER_HD);
                                idsList.add(ii, videoIds);
                                imagesList.add(ii, imageUrl);
                                hdImagesList.add(ii, hdImageUrl);
                            }
                            data.setVideoIds(idsList);
                            data.setImagesList(imagesList);
                            data.setHdImagesList(hdImagesList);
                            if (idsList.size() == 1)
                                data.setSingle(true);
                        }
                        break;
                    }
                    milesDataList.add(data);
                    miles.setMileData(milesDataList);
                }
                milesList.add(j, miles);
            }
        } catch (JSONException ex) {
            Log.e("NewDataParser", "getMiles: ", ex);
        }
        return milesList;
    }
}
