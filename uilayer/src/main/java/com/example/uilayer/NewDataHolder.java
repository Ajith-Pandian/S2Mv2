package com.example.uilayer;

import android.content.Context;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.BulletinMessage;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Schools;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.models.milestones.TMiles;
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
import static com.example.domainlayer.Constants.KEY_SCHOOLS;
import static com.example.domainlayer.Constants.KEY_SECTIONS;
import static com.example.domainlayer.Constants.KEY_TIMESTAMP;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.ROLE_SCL_ADMIN;
import static com.example.domainlayer.Constants.ROLE_TEACHER;
import static com.example.domainlayer.Constants.TYPE_S2M_ADMIN;
import static com.example.domainlayer.Constants.TYPE_SCL_ADMIN;
import static com.example.domainlayer.Constants.TYPE_TEACHER;
import static com.example.domainlayer.Constants.TYPE_T_SCL_ADMIN;
import static com.example.domainlayer.Constants.USER_TYPE_S2M_ADMIN;
import static com.example.domainlayer.Constants.USER_TYPE_SCHOOL;

/**
 * Created by thoughtchimp on 1/24/2017.
 */

public class NewDataHolder {
    private static NewDataHolder mInstance;

    private String enteredUserName;
    private String currentClassName, currentSectionName;
    private Context context;
    private DbUser user, currentNetworkUser;
    private int currentSectionId, currentMilestoneId, currentMileId;
    private ArrayList<TMiles> milesList;
    private ArrayList<TMileData> milesDataList;
    private ArrayList<User> teachersList;
    private ArrayList<Sections> sectionsList;
    private String currentMileTitle;

    private NewDataHolder(Context context) {
        this.context = context;
    }

    public static synchronized NewDataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NewDataHolder(context);
        }
        return mInstance;
    }

    public void setLoginResult(JSONObject userJson) {
        ArrayList<SclActs> sclActList = new ArrayList<>();
        DataParser dataParser = new DataParser();
        try {

            DbUser user = new DbUser();
            user.setId(userJson.getInt(Constants.KEY_ID));
            user.setEmail(userJson.getString(Constants.KEY_EMAIL));
            String lastName = userJson.getString(Constants.KEY_FIRST_NAME);
            if (lastName != null && !lastName.equals("null"))
                user.setFirstName(lastName);
            user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
            user.setPhoneNum(userJson.getString(Constants.KEY_MOBILE_NO));
            user.setAccessToken(userJson.getString(Constants.KEY_ACCESS_TOKEN));
            user.setFirstLogin(userJson.getBoolean(Constants.KEY_IS_FIRST_LOGIN));

            String userType = userJson.getString(Constants.KEY_TYPE);

            user.setAvatar(userJson.getString(Constants.KEY_PROFILE_PICTURE));


            JSONArray schoolsJsonArray = userJson.getJSONArray(KEY_SCHOOLS);
            ArrayList<Schools> schoolsArrayList = new ArrayList<>();
            ArrayList<String> roles = new ArrayList<>();
            for (int i = 0; i < schoolsJsonArray.length(); i++) {
                Schools school = new Schools();
                JSONObject schoolJsonObj = schoolsJsonArray.getJSONObject(i);
                school.setId(schoolJsonObj.getInt(Constants.KEY_ID));
                school.setName(schoolJsonObj.getString(Constants.KEY_NAME));
                school.setLogo(schoolJsonObj.getString(Constants.KEY_LOGO));
                school.setLocality(schoolJsonObj.getString(Constants.KEY_LOCALITY));
                school.setCity(schoolJsonObj.getString(Constants.KEY_CITY));
                roles = getStringsFromArray(schoolJsonObj.getJSONArray(Constants.KEY_ROLES));
                user.setRoles(roles);
                schoolsArrayList.add(school);
            }
            user.setType(getTypeByRoles(userType, roles));
            user.setSchoolsList(schoolsArrayList);
            if (schoolsArrayList.size() > 0) {
                user.setSchoolId(schoolsArrayList.get(0).getId());
                user.setSchoolName(schoolsArrayList.get(0).getName());
            }

            JSONArray sectionsArray = userJson.getJSONArray(KEY_SECTIONS);
            user.setSectionsList(dataParser.getSectionsListFromJson(sectionsArray, true));

            JSONArray schoolActivitiesArray = userJson.getJSONArray(KEY_ACTIVITIES);
            if (schoolActivitiesArray != null && schoolActivitiesArray.length() > 0) {
                //Bulletin
                JSONObject bulletinJson = schoolActivitiesArray.getJSONObject(0);
                SclActs bulletin
                        = new SclActs(bulletinJson.getInt(KEY_ID),
                        bulletinJson.getString(KEY_TYPE),
                        bulletinJson.getString(KEY_MESSAGE),
                        bulletinJson.getString(KEY_TIMESTAMP),
                        bulletinJson.getInt(KEY_LIKES_COUNT),
                        bulletinJson.getBoolean(IS_LIKED));
                user.setBulletin(bulletin);

                for (int i = 1; i < schoolActivitiesArray.length(); i++) {
                    JSONObject schoolActivity = schoolActivitiesArray.getJSONObject(i);
                    SclActs sclActivities
                            = new SclActs(schoolActivity.getInt(KEY_ID),
                            schoolActivity.getString(KEY_TYPE),
                            schoolActivity.getString(KEY_MESSAGE),
                            schoolActivity.getString(KEY_TIMESTAMP),
                            schoolActivity.getInt(KEY_LIKES_COUNT),
                            schoolActivity.getBoolean(IS_LIKED));
                    sclActList.add(sclActivities);
                }
                user.setSclActs(sclActList);
            }
            setUser(user);

            SharedPreferenceHelper.setAccessToken(user.getAccessToken());
            SharedPreferenceHelper.setUserId(user.getId());
            if (schoolsArrayList.size() > 0) {
                SharedPreferenceHelper.setSchoolId(user.getSchoolsList().get(0).getId());
                SharedPreferenceHelper.setSchoolName(user.getSchoolsList().get(0).getName());
            }
            // new DataBaseUtil(context).setUser(user);
        } catch (JSONException exception) {
            Log.e("NewDataHolder", "setLoginResult: ", exception);
        }
    }


    private ArrayList<String> getStringsFromArray(JSONArray stringJsonArray) {
        ArrayList<String> options = new ArrayList<>();
        try {
            for (int i = 0; i < stringJsonArray.length(); i++)
                options.add(i, stringJsonArray.getString(i));
        } catch (JSONException e) {
            Log.d("NewDataParser", "getOptions: ");
        }
        return options;
    }


    private String getTypeByRoles(String userType, ArrayList<String> userRoles) {
        String type = "";

        switch (userType) {
            case USER_TYPE_SCHOOL:
                if (userRoles.contains(ROLE_TEACHER) && userRoles.contains(ROLE_SCL_ADMIN))
                    type = TYPE_T_SCL_ADMIN;
                else if (userRoles.contains(ROLE_SCL_ADMIN))
                    type = TYPE_SCL_ADMIN;
                else if (userRoles.contains(ROLE_TEACHER))
                    type = TYPE_TEACHER;
                break;
            case USER_TYPE_S2M_ADMIN:
                type = TYPE_S2M_ADMIN;
                break;
        }

        return type;
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


    public DbUser getUser() {
        return user;
    }

    public void setUser(DbUser user) {
        this.user = user;
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

    public String getEnteredUserName() {
        return enteredUserName;
    }

    public void setEnteredUserName(String enteredUserName) {
        this.enteredUserName = enteredUserName;
    }

    public DbUser getCurrentNetworkUser() {
        return currentNetworkUser;
    }

    public void setCurrentNetworkUser(DbUser currentNetworkUser) {
        this.currentNetworkUser = currentNetworkUser;
    }


    public ArrayList<TMiles> getMilesList() {
        return milesList;
    }

    public void setMilesList(ArrayList<TMiles> milesList) {
        this.milesList = milesList;
    }

    public ArrayList<TMileData> getMilesDataList() {
        return milesDataList;
    }

    public void setMilesDataList(ArrayList<TMileData> milesDataList) {
        this.milesDataList = milesDataList;
    }

    public String getCurrentMileTitle() {
        return currentMileTitle;
    }

    public void setCurrentMileTitle(String currentMileTitle) {
        this.currentMileTitle = currentMileTitle;
    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public String getCurrentSectionName() {
        return currentSectionName;
    }

    public void setCurrentSectionName(String currentSectionName) {
        this.currentSectionName = currentSectionName;
    }

    public ArrayList<User> getTeachersList() {
        return teachersList;
    }

    public void setTeachersList(ArrayList<User> teachersList) {
        this.teachersList = teachersList;
    }

    public ArrayList<Sections> getSectionsList() {
        return sectionsList;
    }

    public void setSectionsList(ArrayList<Sections> sectionsList) {
        this.sectionsList = sectionsList;
    }
}
