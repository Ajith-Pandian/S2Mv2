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
