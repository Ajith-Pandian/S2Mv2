package com.example.domainlayer.temp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.Bulletin;
import com.example.domainlayer.models.BulletinMessage;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Schools;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.domainlayer.Constants.IS_LIKED;
import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_ACTIVITIES;
import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IMAGE;
import static com.example.domainlayer.Constants.KEY_LIKES_COUNT;
import static com.example.domainlayer.Constants.KEY_LOGO;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.KEY_NAME;
import static com.example.domainlayer.Constants.KEY_SCHOOLS;
import static com.example.domainlayer.Constants.KEY_SCHOOL_ID;
import static com.example.domainlayer.Constants.KEY_SCHOOL_NAME;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_TIMESTAMP;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.SCHOOL_ADMIN;
import static com.example.domainlayer.Constants.SHARED_PREFERENCE;
import static com.example.domainlayer.Constants.TYPE_BULLETIN;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class DataHolder {
    private static DataHolder mInstance;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    String accessToken;
    int schoolId, userId;
    DataParser dataParser;
    SharedPreferences sharedpreferences;
    private ArrayList<Sections> sectionsList;
    private ArrayList<SclActs> sclActList;
    private DbUser user;
    private JSONObject otpSuccessResultJson;
    private JSONObject loginResultJson;

    private DataHolder(Context context) {
        this.context = context;
        sharedpreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        //sharedpreferences = this.context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static synchronized DataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHolder(context);
        }
        return mInstance;
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

    SharedPreferences.Editor getEditor() {
        return editor = sharedpreferences.edit();
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public void setUserId(int userId) {
        this.userId = userId;
        getEditor().putInt(KEY_USER_ID, userId).commit();
    }


    ArrayList<User> networkProfiles;

    public ArrayList<User> getNetworkProfiles() {
        return networkProfiles;
    }

    public void setNetworkProfiles(ArrayList<User> networkProfiles) {
        this.networkProfiles = networkProfiles;
        DbUser user = new DbUser();

    }

    public void saveUserDetails(JSONObject loginResultJson) {
        user = new DbUser();
        dataParser = new DataParser();
        sectionsList = new ArrayList<>();
        sclActList = new ArrayList<>();
        try {
            user.setFirstName(loginResultJson.getString(Constants.KEY_FIRST_NAME));
            user.setId(loginResultJson.getInt(Constants.KEY_ID));
            user.setLastName(loginResultJson.getString(Constants.KEY_LAST_NAME));
            user.setEmail(loginResultJson.getString(Constants.KEY_EMAIL));
            user.setPhoneNum(loginResultJson.getString(Constants.KEY_PHONE_NUM));
           // user.setLastLogin(loginResultJson.getString(Constants.KEY_LAST_LOGIN));
            user.setLastLogin("null");
            user.setSchoolId(loginResultJson.getInt(Constants.KEY_SCHOOL_ID));

           //TODO for s2m and school admin
           /* JSONArray schoolsArray=new JSONArray(loginResultJson.getString(KEY_SCHOOLS));
            ArrayList<Schools> schoolsList=new ArrayList<>();
            for (int i = 0; i < schoolsArray.length(); i++) {
                JSONObject schoolObject=schoolsArray.getJSONObject(i);
                Schools school=new Schools();
                school.setId(schoolObject.getInt(KEY_ID));
                school.setName(schoolObject.getString(KEY_NAME));
                school.setName(schoolObject.getString(KEY_LOGO));
                schoolsList.add(i,school);
            }
            user.setSchoolsList(schoolsList);*/
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
            bulletin.setLiked(bulletinJson.getInt(IS_LIKED));
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
                        schoolActivity.getInt(KEY_LIKES_COUNT),
                        schoolActivity.getInt(IS_LIKED)
                        );
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
ArrayList<User> teachersList;

    public ArrayList<User> getTeachersList() {
        return teachersList;
    }

    public void setTeachersList(ArrayList<User> teachersList) {
        this.teachersList = teachersList;
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
