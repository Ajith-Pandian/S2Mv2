package com.example.uilayer;

import android.content.Context;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.models.User;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.models.milestones.TMiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class DataHolder {
    private static DataHolder mInstance;
    ArrayList<Section> sectionsList;
    int schoolId;
    String avatar;
    String currentMileTitle;
    private String otp;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String accessToken;
    private String lastLogin;
    private String currentClass;
    private String currentSection;
    private String email;
    private JSONObject loginResultJson;
    private JSONObject otpSuccessResultJson;
    private ArrayList<TMiles> milesList;
    private ArrayList<TMileData> currentMileData;
    private ArrayList<TMiles> archiveData;
    private ArrayList<User> networkProfiles;
    private int undoableId;

    public static synchronized DataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHolder();
        }
        return mInstance;
    }

    public ArrayList<TMileData> getCurrentMileData() {
        return currentMileData;
    }

    public void setCurrentMileData(ArrayList<TMileData> currentMileData) {
        this.currentMileData = currentMileData;
    }

    public ArrayList<TMiles> getArchiveData() {
        return archiveData;
    }

    public void setArchiveData(ArrayList<TMiles> archiveData) {
        this.archiveData = archiveData;
    }

    public ArrayList<TMiles> getMilesList() {
        return milesList;
    }

    public void setMilesList(ArrayList<TMiles> milesList) {
        this.milesList = milesList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getUndoableId() {
        return undoableId;
    }

    public void setUndoableId(int undoableId) {
        this.undoableId = undoableId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public JSONObject getLoginResultJson() {
        return loginResultJson;
    }
int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLoginResultJson(JSONObject loginResultJson) {
        this.loginResultJson = loginResultJson;
        try {
            this.userId = loginResultJson.getInt(Constants.KEY_ID);
            this.lastLogin = loginResultJson.getString(Constants.KEY_LAST_LOGIN);
            this.otp = loginResultJson.getString(Constants.KEY_OTP);
            this.email = loginResultJson.getString(Constants.KEY_EMAIL);
            this.phoneNum = loginResultJson.getString(Constants.KEY_PHONE_NUM);
        } catch (JSONException exception) {
            Log.e("DataHolder", "setLoginResultJson: ", exception);
        }
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public JSONObject getOtpSuccessResultJson() {
        return otpSuccessResultJson;
    }

    public void setOtpSuccessResultJson(JSONObject otpSuccessResultJson) {
        this.otpSuccessResultJson = otpSuccessResultJson;
        sectionsList = new ArrayList<>();
        try {
            this.otp = loginResultJson.getString(Constants.KEY_OTP);
            this.firstName = loginResultJson.getString(Constants.KEY_FIRST_NAME);
            this.lastName = loginResultJson.getString(Constants.KEY_LAST_NAME);
            this.email = loginResultJson.getString(Constants.KEY_EMAIL);
            this.phoneNum = loginResultJson.getString(Constants.KEY_PHONE_NUM);
            this.lastLogin = loginResultJson.getString(Constants.KEY_LAST_LOGIN);
            this.schoolId = loginResultJson.getInt(Constants.KEY_SCHOOL_ID);

            this.lastLogin = loginResultJson.getString(Constants.KEY_LAST_LOGIN);


            JSONArray sectionsArray = loginResultJson.getJSONArray(Constants.KEY_SECTIONS);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(i);
                Section section
                        = new Section(sectionObject.getInt(Constants.KEY_ID),
                        sectionObject.getString(Constants.KEY_CLASS),
                        sectionObject.getString(Constants.KEY_SECTION));
                sectionsList.add(section);
            }

        } catch (JSONException exception) {
            Log.e("DataHolder", "setLoginResultJson: ", exception);
        }
    }

    public ArrayList<Section> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(ArrayList<Section> sectionsList) {
        this.sectionsList = sectionsList;
    }


    public ArrayList<User> getNetworkProfiles() {
        return networkProfiles;
    }

    public void setNetworkProfiles(ArrayList<User> networkProfiles) {
        this.networkProfiles = networkProfiles;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(String currentSection) {
        this.currentSection = currentSection;
    }

    public String getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(String currentClass) {
        this.currentClass = currentClass;
    }

    public String getCurrentMileTitle() {
        return currentMileTitle;
    }

    public void setCurrentMileTitle(String currentMileTitle) {
        this.currentMileTitle = currentMileTitle;
    }

    private class Section {
        int id;
        String _class, section;

        Section(int id, String _class, String section) {
            this.id = id;
            this._class = _class;
            this.section = section;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String get_class() {
            return _class;
        }

        public void set_class(String _class) {
            this._class = _class;
        }

        public String getSection() {
            return section;
        }

        public void setSection(String section) {
            this.section = section;
        }
    }
}
