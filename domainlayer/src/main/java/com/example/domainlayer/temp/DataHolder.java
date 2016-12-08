package com.example.domainlayer.temp;

import android.content.Context;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.Bulletin;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Message;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.j256.ormlite.dao.ForeignCollection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.domainlayer.Constants.KEY_ACTIVITIES;
import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_CLASS;
import static com.example.domainlayer.Constants.KEY_COMPLETED_MILESTONES;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IMAGE;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.KEY_MILESTONE_ID;
import static com.example.domainlayer.Constants.KEY_MILESTONE_NAME;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_TIMESTAMP;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TOTAL_MILESTONES;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_USER_ID;
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
     Context context;


    public static synchronized DataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHolder(context);
        }
        return mInstance;
    }


    private DataHolder(Context context)
    {
        this.context=context;
    }
    public void saveUserDetails(JSONObject loginResultJson) {
        user = new DbUser();
        sectionsList = new ArrayList<>();
        sclActList = new ArrayList<>();
        try {
            user.setFirstName(loginResultJson.getString(Constants.KEY_FIRST_NAME));
            user.setLastName(loginResultJson.getString(Constants.KEY_LAST_NAME));
            user.setEmail(loginResultJson.getString(Constants.KEY_EMAIL));
            user.setPhoneNum(loginResultJson.getString(Constants.KEY_PHONE_NUM));
            user.setLastLogin(loginResultJson.getString(Constants.KEY_LAST_LOGIN));
            user.setSchoolId(loginResultJson.getInt(Constants.KEY_SCHOOL_ID));
            user.setWow(loginResultJson.getString(Constants.KEY_WOW));
            user.setAvatar(loginResultJson.getString(Constants.KEY_AVATAR));
            user.setMiles(loginResultJson.getString(Constants.KEY_MILES));

            JSONObject bulletinJson = loginResultJson.getJSONObject(TYPE_BULLETIN);
            Bulletin bulletin = new Bulletin();
            bulletin.setId(bulletinJson.getInt(KEY_ID));
            bulletin.setUserId(bulletinJson.getInt(KEY_USER_ID));
            bulletin.setMsg(getMessage(bulletinJson.getString(KEY_MESSAGE)));
            bulletin.setType(bulletinJson.getString(KEY_TYPE));
            bulletin.setTimeStamp(bulletinJson.getString(KEY_TIMESTAMP));
            user.setBulletin(bulletin);

            JSONArray sectionsArray = loginResultJson.getJSONArray(KEY_SECTIONS);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(i);
                Sections section
                        = new Sections(sectionObject.getInt(KEY_ID),
                        sectionObject.getString(KEY_CLASS),
                        sectionObject.getString(KEY_SECTION),
                        sectionObject.getInt(KEY_COMPLETED_MILESTONES),
                        sectionObject.getInt(KEY_TOTAL_MILESTONES),
                        sectionObject.getInt(KEY_SCHOOL_ID),
                        sectionObject.getString(KEY_MILESTONE_NAME),
                        sectionObject.getInt(KEY_MILESTONE_ID));
                sectionsList.add(section);
            }
            user.setSectionsList(sectionsList);

            JSONArray schoolActivities = loginResultJson.getJSONArray(KEY_ACTIVITIES);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject schoolActivity = schoolActivities.getJSONObject(i);
                SclActs sclActivities
                        = new SclActs(schoolActivity.getInt(KEY_ID),
                        schoolActivity.getInt(KEY_USER_ID),schoolActivity.getString(KEY_MESSAGE),
                        //getMessage(schoolActivity.getString(KEY_MESSAGE)),
                        schoolActivity.getString(KEY_TYPE),
                        schoolActivity.getString(KEY_TIMESTAMP));
                sclActList.add(sclActivities);
            }
            user.setSclActs(sclActList);

            new DataBaseUtil(context).setUser(user);


        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }


    Message getMessage(String message)
    { Message msg=null;

        try{
            JSONObject msgObject=new JSONObject(message);
            msg=new Message(msgObject.getString(KEY_TITLE),
                    msgObject.getString(KEY_IMAGE),
                    msgObject.getString(KEY_BODY));
        }
        catch (JSONException ex){
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
