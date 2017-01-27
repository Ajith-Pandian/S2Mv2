package com.example.uilayer;

import android.content.Context;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.Bulletin;
import com.example.domainlayer.models.BulletinMessage;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.temp.DataParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.domainlayer.Constants.IS_LIKED;
import static com.example.domainlayer.Constants.KEY_ACTIVITIES;
import static com.example.domainlayer.Constants.KEY_BODY;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_IMAGE;
import static com.example.domainlayer.Constants.KEY_LIKES_COUNT;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_TIMESTAMP;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.KEY_USER_ID;
import static com.example.domainlayer.Constants.TYPE_BULLETIN;

/**
 * Created by thoughtchimp on 1/24/2017.
 */

public class NewDataHolder {
    private static NewDataHolder mInstance;
    private int userId,schoolId;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String accessToken;
    private String userType;
    private String lastLogin;
    private String email;
    private Context context;
    private DbUser user;
    private int currentSectionId,currentMilestoneId,currentMileId;

    private NewDataHolder(Context context) {
        this.context = context;
    }

    public static synchronized NewDataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NewDataHolder(context);
        }
        return mInstance;
    }

    private String avatar;
    private String schoolName;

    public void setLoginResult(JSONObject userJson) {
        ArrayList<SclActs> sclActList = new ArrayList<>();
        DataParser dataParser = new DataParser();
        try {
            this.userId = userJson.getInt(Constants.KEY_ID);
             this.lastLogin = userJson.getString(Constants.KEY_LAST_LOGIN);
            //this.lastLogin = "null";
            // this.otp = userJson.getString(Constants.KEY_OTP);
            this.email = userJson.getString(Constants.KEY_EMAIL);
            this.firstName = userJson.getString(Constants.KEY_FIRST_NAME);
            this.lastName = userJson.getString(Constants.KEY_LAST_NAME);
            this.phoneNum = userJson.getString(Constants.KEY_PHONE_NUM);
            this.accessToken = userJson.getString(Constants.KEY_ACCESS_TOKEN);
            this.userType = userJson.getString(Constants.KEY_TYPE);
            this.avatar = userJson.getString(Constants.KEY_AVATAR);
            this.schoolName = userJson.getString(Constants.KEY_SCHOOL_NAME);
            this.schoolId = userJson.getInt(Constants.KEY_SCHOOL_ID);
            SharedPreferenceHelper.setAccessToken(accessToken);
            SharedPreferenceHelper.setSchoolId(schoolId);
            SharedPreferenceHelper.setUserId(userId);
            DbUser user = new DbUser();
            user.setId(userId);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNum(phoneNum);
            user.setAccessToken(accessToken);
            user.setLastLogin(lastLogin);
            user.setType(userType);
            user.setAvatar(avatar);
            user.setSchoolId(schoolId);
            user.setSchoolName(schoolName);

            JSONObject bulletinJson = userJson.getJSONObject(TYPE_BULLETIN);
            Bulletin bulletin = new Bulletin();
            bulletin.setId(bulletinJson.getInt(KEY_ID));
            bulletin.setUserId(bulletinJson.getInt(KEY_USER_ID));
            bulletin.setMsg(getMessage(bulletinJson.getString(KEY_MESSAGE)));
            bulletin.setType(bulletinJson.getString(KEY_TYPE));
            bulletin.setTimeStamp(bulletinJson.getString(KEY_TIMESTAMP));
            bulletin.setLiked(bulletinJson.getInt(IS_LIKED));
            user.setBulletin(bulletin);

            JSONArray sectionsArray = userJson.getJSONArray(KEY_SECTIONS);
            user.setSectionsList(dataParser.getSectionsListFromJson(sectionsArray));

            JSONArray schoolActivities = userJson.getJSONArray(KEY_ACTIVITIES);

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
            setUser(user);
            new DataBaseUtil(context).setUser(user);
        } catch (JSONException exception) {
            Log.e("NewDataHolder", "setLoginResult: ", exception);
        }
    }

    public void setVerifyDetails(JSONObject verifyDetailsJson) {
        try {
            this.userId = verifyDetailsJson.getInt(Constants.KEY_ID);
            // this.lastLogin = verifyDetailsJson.getString(Constants.KEY_LAST_LOGIN);
            // this.otp = verifyDetailsJson.getString(Constants.KEY_OTP);
            this.email = verifyDetailsJson.getString(Constants.KEY_EMAIL);
            this.firstName = verifyDetailsJson.getString(Constants.KEY_FIRST_NAME);
            this.lastName = verifyDetailsJson.getString(Constants.KEY_LAST_NAME);
            this.phoneNum = verifyDetailsJson.getString(Constants.KEY_PHONE_NUM);
            // this.accessToken = verifyDetailsJson.getString(Constants.KEY_ACCESS_TOKEN);
            // this.userType = verifyDetailsJson.getString(Constants.KEY_TYPE);
            DbUser user = new DbUser();
            user.setId(userId);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNum(phoneNum);
        } catch (JSONException exception) {
            Log.e("NewDataHolder", "setVerifyDetails: ", exception);
        }
    }


    public void saveUserInDb(JSONObject loginResultJson) {
        DbUser user = new DbUser();
        DataParser dataParser = new DataParser();
        ArrayList<Sections> sectionsList = new ArrayList<>();
        ArrayList<SclActs> sclActList = new ArrayList<>();
        try {
            user.setFirstName(loginResultJson.getString(Constants.KEY_FIRST_NAME));
            user.setId(loginResultJson.getInt(Constants.KEY_ID));
            user.setLastName(loginResultJson.getString(Constants.KEY_LAST_NAME));
            user.setEmail(loginResultJson.getString(Constants.KEY_EMAIL));
            user.setPhoneNum(loginResultJson.getString(Constants.KEY_PHONE_NUM));
            user.setLastLogin(loginResultJson.getString(Constants.KEY_LAST_LOGIN));
            //user.setLastLogin("null");
            user.setSchoolId(loginResultJson.getInt(Constants.KEY_SCHOOL_ID));

            user.setSchoolName(loginResultJson.getString(Constants.KEY_SCHOOL_NAME));

            user.setWow(loginResultJson.getString(Constants.KEY_WOW));
            user.setAvatar(loginResultJson.getString(Constants.KEY_AVATAR));
            user.setMiles(loginResultJson.getString(Constants.KEY_MILES));
            user.setType(loginResultJson.getString(Constants.KEY_TYPE));

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

            // new DataBaseUtil(context).setUser(user);
            setUser(user);
        } catch (JSONException exception) {
            Log.e("DataHolder", "saveUserDetails: ", exception);
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private BulletinMessage getMessage(String message) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DbUser getUser() {
        return user;
    }

    public void setUser(DbUser user) {
        this.user = user;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getCurrentSectionId() {
        return currentSectionId;
    }

    public void setCurrentSectionId(int currentSectionId) {
        this.currentSectionId = currentSectionId;
    }

    public int getCurrentMilestoneId() {
        return currentMilestoneId;
    }

    public void setCurrentMilestoneId(int currentMilestoneId) {
        this.currentMilestoneId = currentMilestoneId;
    }

    public int getCurrentMileId() {
        return currentMileId;
    }

    public void setCurrentMileId(int currentMileId) {
        this.currentMileId = currentMileId;
    }
}
