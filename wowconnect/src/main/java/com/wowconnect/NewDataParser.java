package com.wowconnect;

import android.content.Context;
import android.util.Log;

import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.models.DbUser;
import com.wowconnect.models.Milestones;
import com.wowconnect.models.S2mConfiguration;
import com.wowconnect.models.Schools;
import com.wowconnect.models.mcq.MCQs;
import com.wowconnect.models.mcq.McqOptions;
import com.wowconnect.models.milestones.TMileData;
import com.wowconnect.models.milestones.TMiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.wowconnect.domain.Constants.KEY_NAME;


/**
 * Created by Ajit on 01-02-2017.
 */

public class NewDataParser {
    private S2mConfiguration configuration;

    public S2mConfiguration getS2mConfiguration() {
        configuration = new S2mConfiguration();
        try {
            JSONObject configObj = new JSONObject(SharedPreferenceHelper.getConfiguration());

            configuration.setSchoolsArrayList(getSchoolsList(configObj.getJSONArray(Constants.KEY_SCHOOLS)));

            JSONObject feedbackJson = configObj.getJSONObject(Constants.KEY_FEEDBACK_QUESTIONS);

            JSONObject milesJson = feedbackJson.getJSONObject(Constants.KEY_MILES);
            getDetails(milesJson, true);

            JSONObject trainingJson = feedbackJson.getJSONObject(Constants.KEY_TRAINING);
            getDetails(trainingJson, false);

            configuration.setMilestonesArrayList(getMilestonesList(configObj.getJSONArray(Constants.KEY_MILESTONES)));

            configuration.setCountryCodes(getStringsFromArray(configObj.getJSONArray(Constants.KEY_COUNTRY_CODES)));

            configuration.setColorSchemes(getStringsFromArray(configObj.getJSONArray(Constants.KEY_COLOR_SCHEME)));
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
            schools.setId(schoolJson.getInt(Constants.KEY_ID));
            schools.setName(schoolJson.getString(KEY_NAME));
            schoolsList.add(schools);
        }
        return schoolsList;
    }

    private ArrayList<Milestones> getMilestonesList(JSONArray milestonesJsonArray) throws JSONException {
        ArrayList<Milestones> milestonesArrayList = new ArrayList<>();
        for (int i = 0; i < milestonesJsonArray.length(); i++) {
            JSONObject milestonesJson = milestonesJsonArray.getJSONObject(i);
            milestonesArrayList.add(new Milestones(milestonesJson.getInt(Constants.KEY_ID), milestonesJson.getString(KEY_NAME)));
        }
        return milestonesArrayList;
    }

    private void getDetails(JSONObject mileTrainingObj, boolean isMile) {
        try {
            String question = mileTrainingObj.getString(Constants.KEY_QUESTION);

            JSONObject thumbsUpObj = mileTrainingObj.getJSONObject(Constants.KEY_THUMBS_UP);
            String thumbsUpQuestion = thumbsUpObj.getString(Constants.KEY_QUESTION);
            ArrayList<String> thumbsUpOptions = getOptions(thumbsUpObj.getJSONArray(Constants.KEY_OPTIONS));

            JSONObject thumbsDownObj = mileTrainingObj.getJSONObject(Constants.KEY_THUMBS_DOWN);
            String thumbsDownQuestion = thumbsDownObj.getString(Constants.KEY_QUESTION);
            ArrayList<String> thumbsDownOptions = getOptions(thumbsDownObj.getJSONArray(Constants.KEY_OPTIONS));
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
                String type = milesJson.getString(Constants.KEY_TYPE);
                if (type.equals(Constants.KEY_MILE)) {
                   /* if (isFirstMile) {
                        isFirstMile = false;
                        completable = true;
                    } else {
                        completable = false;
                    }*/
                    /*if (isArchive) {
                        // archiveIndex++;
                        miles.setMileIndex(++archiveIndex);
                    }*/
                    if (!milesJson.isNull(Constants.KEY_CONTENT_INDEX))
                        miles.setMileIndex(milesJson.getInt(Constants.KEY_CONTENT_INDEX));
                    if (!milesJson.isNull(Constants.KEY_IS_COMPLETED))
                        completable = !milesJson.getBoolean(Constants.KEY_IS_COMPLETED);
                } else {
                    if (milesJson.has(Constants.KEY_MCQS) &&
                            !milesJson.isNull(Constants.KEY_MCQS) &&
                            milesJson.getJSONArray(Constants.KEY_MCQS).length() > 0)
                        miles.setMcqs(getMcqs(milesJson.getJSONArray(Constants.KEY_MCQS)));
                    if (!milesJson.isNull(Constants.KEY_IS_COMPLETED))
                        completable = !milesJson.getBoolean(Constants.KEY_IS_COMPLETED);
                }
                miles.setId(milesJson.getInt(Constants.KEY_ID));
                miles.setType(milesJson.getString(Constants.KEY_TYPE));
                miles.setTitle(milesJson.getString(Constants.KEY_TITLE));
                miles.setNote(milesJson.getString(Constants.KEY_DESCRIPTION));

                miles.setCompletable(completable);


                JSONArray milesDataJsonArray = milesJson.getJSONArray(Constants.KEY_CONTENT_DATA);
                ArrayList<TMileData> milesDataList = new ArrayList<>();
                for (int i = 0; i < milesDataJsonArray.length(); i++) {
                    JSONObject dataJson = milesDataJsonArray.getJSONObject(i);
                    String dataType = dataJson.getString(Constants.KEY_TYPE);
                    TMileData data = new TMileData(dataJson.getString(Constants.KEY_TITLE), dataType);


                    switch (dataType) {
                        case Constants.TYPE_TEXT:
                            data.setBody(dataJson.getString(Constants.KEY_BODY));
                            break;
                        case Constants.TYPE_IMAGE: {
                            ArrayList<String> urlsList = new ArrayList<>();
                            JSONArray imageUrlsArray = new JSONArray(dataJson.getString(Constants.KEY_BODY));
                            for (int ii = 0; ii < imageUrlsArray.length(); ii++) {
                                String url = imageUrlsArray.getString(ii);
                                urlsList.add(url);
                            }
                            data.setUrlsList(urlsList);
                            if (urlsList.size() == 1)
                                data.setSingle(true);
                        }
                        break;
                        case Constants.TYPE_AUDIO: {
                            ArrayList<String> urlsList = new ArrayList<>();
                            ArrayList<String> imagesList = new ArrayList<>();
                            JSONArray audioUrlsArray = new JSONArray(dataJson.getString(Constants.KEY_BODY));
                            for (int ii = 0; ii < audioUrlsArray.length(); ii++) {
                                JSONObject audioData = audioUrlsArray.getJSONObject(ii);
                                String audioUrl = audioData.getString(Constants.KEY_AUDIO_URL);
                                String imageUrl = audioData.getString(Constants.KEY_AUDIO_POSTER);
                                urlsList.add(ii, audioUrl);
                                imagesList.add(ii, imageUrl);
                            }
                            data.setUrlsList(urlsList);
                            data.setImagesList(imagesList);
                            if (urlsList.size() == 1)
                                data.setSingle(true);
                        }
                        break;
                        case Constants.TYPE_VIDEO: {
                            ArrayList<String> idsList = new ArrayList<>();
                            ArrayList<String> imagesList = new ArrayList<>();
                            ArrayList<String> hdImagesList = new ArrayList<>();
                            JSONArray videoUrlsArray = new JSONArray(dataJson.getString(Constants.KEY_BODY));
                            for (int ii = 0; ii < videoUrlsArray.length(); ii++) {
                                JSONObject audioData = videoUrlsArray.getJSONObject(ii);
                                String videoIds = audioData.getString(Constants.KEY_VIDEO_ID);
                                String imageUrl = audioData.getString(Constants.KEY_VIDEO_POSTER);
                                String hdImageUrl = audioData.getString(Constants.KEY_VIDEO_POSTER_HD);
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
                }
                miles.setMileData(milesDataList);
                milesList.add(j, miles);
            }
        } catch (JSONException ex) {
            Log.e("NewDataParser", "getMiles: ", ex);
        }
        return milesList;
    }

    public ArrayList<String> getUserRoles(Context context, int userId) {
        DbUser user = new DataBaseUtil(context).getUser(userId);
        ArrayList<String> userRoles = new ArrayList<>();
        try {
            JSONArray userRolesArray = new JSONArray(user.getRoles());
            for (int i = 0; i < userRolesArray.length(); i++) {
                userRoles.add(userRolesArray.getString(i));
            }
        } catch (Exception e) {
            Log.d("NewDataParser", "getUserRoles: " + e.toString());
        }
        return userRoles;
    }

    private ArrayList<MCQs> getMcqs(JSONArray mcqsJsonArray) {
        ArrayList<MCQs> mcqsArrayList = new ArrayList<>();

        try {
            if (mcqsJsonArray != null && mcqsJsonArray.length() > 0) {
                for (int i = 0; i < mcqsJsonArray.length(); i++) {
                    JSONObject mcqsJson = mcqsJsonArray.getJSONObject(i);
                    MCQs mcq = new MCQs();
                    mcq.setId(mcqsJson.getInt(Constants.KEY_ID));
                    mcq.setAnswer(mcqsJson.getString(Constants.KEY_ANSWER));
                    mcq.setQuestion(mcqsJson.getString(Constants.KEY_QUESTION));
                    ArrayList<McqOptions> optionsList = new ArrayList<>();

                    JSONArray optionsArray = new JSONArray(mcqsJson.getString(Constants.KEY_OPTIONS));
                    for (int ii = 0; ii < optionsArray.length(); ii++) {
                        String optionText = optionsArray.getString(ii);
                        McqOptions options = new McqOptions();
                        options.setText(optionText);
                        optionsList.add(options);
                    }

                    mcq.setOptions(optionsList);
                    mcqsArrayList.add(i, mcq);
                }
            }
        } catch (Exception e) {
            Log.d("NewDataParser", "getMcqs: " + e.toString());
        }
        return mcqsArrayList;
    }
}
