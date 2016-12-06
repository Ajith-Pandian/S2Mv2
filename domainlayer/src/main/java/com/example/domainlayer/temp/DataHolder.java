package com.example.domainlayer.temp;

import android.content.Context;
import android.util.Log;

import com.example.domainlayer.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.domainlayer.Constants.KEY_ACTIVITIES;
import static com.example.domainlayer.Constants.KEY_CLASS;
import static com.example.domainlayer.Constants.KEY_COMPLETED_MILESTONES;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_TIMESTAMP;
import static com.example.domainlayer.Constants.KEY_TOTAL_MILESTONES;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.TYPE_BULLETIN;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class DataHolder {
    private static DataHolder mInstance;
    private ArrayList<Section> sectionsList;
    private ArrayList<SclActivities> sclActList;
    private User user;
    private JSONObject otpSuccessResultJson;
    private JSONObject loginResultJson;


    public static synchronized DataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHolder();
        }
        return mInstance;
    }

    public void saveUserDetails(JSONObject otpSuccessResultJson) {
        this.otpSuccessResultJson = otpSuccessResultJson;
        user = new User();
        sectionsList = new ArrayList<>();
        sclActList = new ArrayList<>();
        try {
            user.setOtp(loginResultJson.getString(Constants.KEY_OTP));
            user.setFirstName(loginResultJson.getString(Constants.KEY_FIRST_NAME));
            user.setLastName(loginResultJson.getString(Constants.KEY_LAST_NAME));
            user.setEmail(loginResultJson.getString(Constants.KEY_EMAIL));
            user.setPhoneNum(loginResultJson.getString(Constants.KEY_PHONE_NUM));
            user.setLastLogin(loginResultJson.getString(Constants.KEY_LAST_LOGIN));
            user.setSchoolId(loginResultJson.getInt(Constants.KEY_SCHOOL_ID));

            JSONObject bulletinJson = loginResultJson.getJSONObject(TYPE_BULLETIN);
            SclActivities bulletin = new SclActivities();
            bulletin.setId(bulletinJson.getInt(KEY_ID));
            bulletin.setUserId(bulletinJson.getInt(KEY_USER_ID));
            bulletin.setMsg(bulletinJson.getString(KEY_MESSAGE));
            bulletin.setType(bulletinJson.getString(KEY_TYPE));
            bulletin.setTimeStamp(bulletinJson.getString(KEY_TIMESTAMP));
            user.setBulletin(bulletin);

            JSONArray sectionsArray = loginResultJson.getJSONArray(KEY_SECTIONS);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(i);
                Section section
                        = new Section(sectionObject.getInt(KEY_ID),
                        sectionObject.getString(KEY_CLASS),
                        sectionObject.getString(KEY_SECTION),
                        sectionObject.getInt(KEY_COMPLETED_MILESTONES),
                        sectionObject.getInt(KEY_TOTAL_MILESTONES));
                sectionsList.add(section);
            }
            user.setSections(sectionsList);

            JSONArray schoolActivities = loginResultJson.getJSONArray(KEY_ACTIVITIES);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject schoolActivity = schoolActivities.getJSONObject(i);
                SclActivities sclActivities
                        = new SclActivities(schoolActivity.getInt(KEY_ID),
                        schoolActivity.getInt(KEY_USER_ID),
                        schoolActivity.getString(KEY_MESSAGE),
                        schoolActivity.getString(KEY_TYPE),
                        schoolActivity.getString(KEY_TIMESTAMP));
                sclActList.add(sclActivities);
            }
            user.setSclActs(sclActList);



        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }

    public ArrayList<Section> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(ArrayList<Section> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public ArrayList<SclActivities> getSclActList() {
        return sclActList;
    }

    public void setSclActList(ArrayList<SclActivities> sclActList) {
        this.sclActList = sclActList;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public JSONObject getOtpSuccessResultJson() {
        return otpSuccessResultJson;
    }

    private class User {
        int schoolId;
        private String otp;
        private String firstName;
        private String lastName;
        private String phoneNum;
        private String accessToken;
        private String lastLogin;
        private String email;
        private ArrayList<Section> sections;
        private ArrayList<SclActivities> sclActs;
        private SclActivities bulletin;

        public ArrayList<Section> getSections() {
            return sections;
        }

        public void setSections(ArrayList<Section> sections) {
            this.sections = sections;
        }

        public ArrayList<SclActivities> getSclActs() {
            return sclActs;
        }

        public void setSclActs(ArrayList<SclActivities> sclActs) {
            this.sclActs = sclActs;
        }

        public SclActivities getBulletin() {
            return bulletin;
        }

        public void setBulletin(SclActivities bulletin) {
            this.bulletin = bulletin;
        }

        public int getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(int schoolId) {
            this.schoolId = schoolId;
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


    }

    private class Section {
        int id, comp_ms, tot_ms;
        String _class, section;

        public Section(int id, String _class, String section, int comp_ms, int tot_ms) {
            this.id = id;
            this.comp_ms = comp_ms;
            this.tot_ms = tot_ms;
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

    private class SclActivities {
        int id, userId;
        String msg, type, timeStamp;

        public SclActivities(int id, int userId, String msg, String type, String timeStamp) {
            this.id = id;
            this.userId = userId;
            this.msg = msg;
            this.type = type;
            this.timeStamp = timeStamp;
        }

        private SclActivities() {

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }
    }
}
