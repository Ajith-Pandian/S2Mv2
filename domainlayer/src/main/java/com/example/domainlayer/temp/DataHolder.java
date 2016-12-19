package com.example.domainlayer.temp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.Bulletin;
import com.example.domainlayer.models.BulletinMessage;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_ACTIVITIES;
import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IMAGE;
import static com.example.domainlayer.Constants.KEY_LIKES_COUNT;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SCHOOL_NAME;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_TIMESTAMP;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.SHARED_PREFERENCE;
import static com.example.domainlayer.Constants.TYPE_BULLETIN;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class DataHolder {
    private static DataHolder mInstance;
    private ArrayList<Sections> sectionsList;
    private ArrayList<SclActs> sclActList;
    private DbUser user;
    private JSONObject otpSuccessResultJson;
    private JSONObject loginResultJson;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    String accessToken;
    int schoolId, userId;

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void setUserId(int userId) {
        this.userId = userId;
        getEditor().putInt(KEY_USER_ID, userId);

    }

    public int getSchoolId() {
        return sharedPreferences.getInt(KEY_SCHOOL_ID, -1);

    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
        getEditor().putInt(KEY_SCHOOL_ID, schoolId);
    }

    public String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        getEditor().putString(KEY_ACCESS_TOKEN, accessToken);
    }

    public static synchronized DataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHolder(context);
        }
        return mInstance;
    }


    DataParser dataParser;
    SharedPreferences sharedpreferences;

    private DataHolder(Context context) {
        this.context = context;

    }

    SharedPreferences.Editor getEditor() {
        sharedpreferences = this.context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return editor = sharedpreferences.edit();
    }

    public void saveUserDetails(JSONObject loginResultJson) {
        user = new DbUser();
        dataParser = new DataParser();
        sectionsList = new ArrayList<>();
        sclActList = new ArrayList<>();
        try {
            user.setFirstName(loginResultJson.getString(Constants.KEY_FIRST_NAME));
            user.setLastName(loginResultJson.getString(Constants.KEY_LAST_NAME));
            user.setEmail(loginResultJson.getString(Constants.KEY_EMAIL));
            user.setPhoneNum(loginResultJson.getString(Constants.KEY_PHONE_NUM));
            user.setLastLogin(loginResultJson.getString(Constants.KEY_LAST_LOGIN));
            user.setSchoolId(loginResultJson.getInt(Constants.KEY_SCHOOL_ID));
            user.setSchoolName(loginResultJson.getString(Constants.KEY_SCHOOL_NAME));

            user.setWow(loginResultJson.getString(Constants.KEY_WOW));
            user.setAvatar(loginResultJson.getString(Constants.KEY_AVATAR));
            user.setMiles(loginResultJson.getString(Constants.KEY_MILES));
            user.setType(loginResultJson.getString(Constants.KEY_TYPE));


            //Shared preferences
            setSchoolId(loginResultJson.getInt(Constants.KEY_SCHOOL_ID));
            setUserId(loginResultJson.getInt(Constants.KEY_ID));
            setAccessToken(loginResultJson.getString(Constants.KEY_ACCESS_TOKEN));

            JSONObject bulletinJson = loginResultJson.getJSONObject(TYPE_BULLETIN);
            Bulletin bulletin = new Bulletin();
            bulletin.setId(bulletinJson.getInt(KEY_ID));
            bulletin.setUserId(bulletinJson.getInt(KEY_USER_ID));
            bulletin.setMsg(getMessage(bulletinJson.getString(KEY_MESSAGE)));
            bulletin.setType(bulletinJson.getString(KEY_TYPE));
            bulletin.setTimeStamp(bulletinJson.getString(KEY_TIMESTAMP));
            user.setBulletin(bulletin);

            JSONArray sectionsArray = loginResultJson.getJSONArray(KEY_SECTIONS);
            user.setSectionsList(dataParser.getSectionsListFromJson(sectionsArray));

            JSONArray schoolActivities = loginResultJson.getJSONArray(KEY_ACTIVITIES);

            for (int i = 0; i < schoolActivities.length(); i++) {
                JSONObject schoolActivity = schoolActivities.getJSONObject(i);
                SclActs sclActivities
                        = new SclActs(schoolActivity.getInt(KEY_ID),
                        schoolActivity.getInt(KEY_USER_ID),
                        "",
                       // schoolActivity.getString(KEY_SCHOOL_NAME),
                        schoolActivity.getString(KEY_MESSAGE),
                        //getMessage(schoolActivity.getString(KEY_MESSAGE)),
                        schoolActivity.getString(KEY_TYPE),
                        schoolActivity.getString(KEY_TIMESTAMP),
                        schoolActivity.getInt(KEY_LIKES_COUNT));
                sclActList.add(sclActivities);
            }
            user.setSclActs(sclActList);

            new DataBaseUtil(context).setUser(user);


        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }


    BulletinMessage getMessage(String message) {
        BulletinMessage msg = null;

        try {
            JSONObject msgObject = new JSONObject(message);
            msg = new BulletinMessage(msgObject.getString(KEY_TITLE),
                    msgObject.getString(KEY_IMAGE),
                    msgObject.getString(KEY_BODY));
        } catch (JSONException ex) {
            Log.e("GetMsg", "getMessage: ", ex);
        }
        return msg;
    }

    public ArrayList<Sections> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(ArrayList<Sections> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public ArrayList<SclActs> getSclActList() {
        return sclActList;
    }

    public void setSclActList(ArrayList<SclActs> sclActList) {
        this.sclActList = sclActList;
    }

    public DbUser getUser() {
        return this.user;
    }

    public void setUser(DbUser user) {
        this.user = user;
    }

    public JSONObject getOtpSuccessResultJson() {
        return otpSuccessResultJson;
    }
}
