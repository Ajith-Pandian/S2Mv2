package com.example.uilayer;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.uilayer.Constants.KEY_CLASS;
import static com.example.uilayer.Constants.KEY_SECTION;
import static com.example.uilayer.Constants.KEY_SECTIONS;
import static com.example.uilayer.Constants.KEY__ID;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class DataHolder {
    private static DataHolder mInstance;
    ArrayList<Section> sectionsList;
    int schoolId;
    private String otp;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String accessToken;
    private String lastLogin;
    private String email;
    private JSONObject loginResultJson;
    private JSONObject otpSuccessResultJson;

    public static synchronized DataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHolder();
        }
        return mInstance;
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

    public void setLoginResultJson(JSONObject loginResultJson) {
        this.loginResultJson = loginResultJson;
        try {
            this.otp = loginResultJson.getString(Constants.KEY_OTP);
            this.firstName = loginResultJson.getString(Constants.KEY_FIRST_NAME);
            this.lastName = loginResultJson.getString(Constants.KEY_LAST_NAME);
            this.email = loginResultJson.getString(Constants.KEY_EMAIL);
            this.phoneNum = loginResultJson.getString(Constants.KEY_PHONE_NUM);
            this.lastLogin = loginResultJson.getString(Constants.KEY_LAST_LOGIN);
        } catch (JSONException exception) {
            Log.e("DataHolder", "setLoginResultJson: ", exception);
        }
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


            JSONArray sectionsArray = loginResultJson.getJSONArray(KEY_SECTIONS);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(i);
                Section section
                        = new Section(sectionObject.getInt(KEY__ID),
                        sectionObject.getString(KEY_CLASS),
                        sectionObject.getString(KEY_SECTION));
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

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
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
