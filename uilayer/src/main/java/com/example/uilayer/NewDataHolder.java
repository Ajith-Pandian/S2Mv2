package com.example.uilayer;

import android.content.Context;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Schools;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.example.domainlayer.models.milestones.TMileData;
import com.example.domainlayer.models.milestones.TMiles;
import com.example.domainlayer.temp.DataParser;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.example.domainlayer.Constants.IS_LIKED;
import static com.example.domainlayer.Constants.KEY_ACTIVITIES;
import static com.example.domainlayer.Constants.KEY_BULLETIN_BOARD;
import static com.example.domainlayer.Constants.KEY_ICON;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_LIKES_COUNT;
import static com.example.domainlayer.Constants.KEY_MESSAGE;
import static com.example.domainlayer.Constants.KEY_OPTIONS;
import static com.example.domainlayer.Constants.KEY_SCHOOLS;
import static com.example.domainlayer.Constants.KEY_TIMESTAMP;
import static com.example.domainlayer.Constants.KEY_TITLE;
import static com.example.domainlayer.Constants.KEY_TYPE;
import static com.example.domainlayer.Constants.TYPE_BULLETIN;

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
    private ArrayList<TMiles> introTrainingsList;
    private ArrayList<TMiles> archiveList;
    private ArrayList<TMileData> milesDataList, introDataList;
    private ArrayList<User> teachersList;
    private ArrayList<DbUser> networkUsers;
    private ArrayList<Sections> sectionsList;
    private ArrayList<SclActs> sclActList;
    private SclActs bulletin;
    private String currentMileTitle;
    private Map<String, String> resultsMap;

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

        try {

            DbUser user = new DbUser();
            user.setId(userJson.getInt(Constants.KEY_ID));
            user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
            if (!userJson.isNull(Constants.KEY_LAST_NAME))
                user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
            user.setPhoneNum(userJson.getString(Constants.KEY_MOBILE_NO));
            user.setEmail(userJson.getString(Constants.KEY_EMAIL));
            user.setAccessToken(userJson.getString(Constants.KEY_ACCESS_TOKEN));
            // user.setFirstLogin(userJson.getBoolean(Constants.KEY_IS_FIRST_LOGIN));
            user.setFirstLogin(true);

            String userType = userJson.getString(Constants.KEY_TYPE);

            user.setAvatar(userJson.getString(Constants.KEY_PROFILE_PICTURE));

            if (!userJson.isNull(Constants.KEY_OPTIONS)) {
                JSONObject optionsObject = userJson.getJSONObject(KEY_OPTIONS);
                if (!optionsObject.isNull(Constants.KEY_ANNIVERSARY))
                    user.setAnniversary(optionsObject.getString(Constants.KEY_ANNIVERSARY));
                if (!optionsObject.isNull(Constants.KEY_GENDER))
                    user.setGender(optionsObject.getString(Constants.KEY_GENDER));
                if (!optionsObject.isNull(Constants.KEY_DOB))
                    user.setDob(optionsObject.getString(Constants.KEY_DOB));
            }
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
                //roles = ;
                //user.setRoles(roles);
                if (i == 0)
                    SharedPreferenceHelper.setUserRoles(getStringsFromArray(schoolJsonObj.getJSONArray(Constants.KEY_ROLES)));
                schoolsArrayList.add(school);
            }
            user.setType(userType);
            user.setSchoolsList(schoolsArrayList);
            if (schoolsArrayList.size() > 0) {
                user.setSchoolId(schoolsArrayList.get(0).getId());
                user.setSchoolName(schoolsArrayList.get(0).getName());
                SharedPreferenceHelper.setSchoolId(user.getSchoolsList().get(0).getId());
                SharedPreferenceHelper.setSchoolName(user.getSchoolsList().get(0).getName());
                SharedPreferenceHelper.setSchoolIamge(user.getSchoolsList().get(0).getLogo());
            }

          /* */
            // setUser(user);

            SharedPreferenceHelper.setAccessToken(user.getAccessToken());
            SharedPreferenceHelper.setUserId(user.getId());

            user.setLoggedIn(true);
            SharedPreferenceHelper.setLoginStatus(true);
            new NetworkHelper(context).sendFirebaseTokenToServer(FirebaseInstanceId.getInstance().getToken());
            new DataBaseUtil(context).setUser(user);
        } catch (JSONException exception) {
            Log.e("NewDataHolder", "setLoginResult: ", exception);
        }
    }


    public void saveDashboardDetails(String dashBoardString) {
        ArrayList<SclActs> sclActList = new ArrayList<>();
        try {
            JSONObject dashBoardJson = new JSONObject(dashBoardString);

            if (!dashBoardJson.isNull(KEY_BULLETIN_BOARD)) {

                JSONObject bulletinJson = dashBoardJson.getJSONObject(KEY_BULLETIN_BOARD);
                SclActs bulletin
                        = new SclActs(bulletinJson.getInt(KEY_ID),
                        bulletinJson.getString(KEY_TYPE),
                        bulletinJson.getString(KEY_TITLE),
                        bulletinJson.getString(KEY_MESSAGE),
                        bulletinJson.getString(KEY_TIMESTAMP),
                        bulletinJson.getInt(KEY_LIKES_COUNT),
                        bulletinJson.getBoolean(IS_LIKED),
                        bulletinJson.getString(KEY_ICON)
                );
                //user.setBulletin(bulletin);
                setBulletin(bulletin);
            }

            JSONArray schoolActivitiesArray = dashBoardJson.getJSONArray(KEY_ACTIVITIES);
            if (schoolActivitiesArray != null && schoolActivitiesArray.length() > 0) {
                for (int i = 1; i < schoolActivitiesArray.length(); i++) {
                    JSONObject schoolActivity = schoolActivitiesArray.getJSONObject(i);
                    SclActs sclActivity
                            = new SclActs(schoolActivity.getInt(KEY_ID),
                            schoolActivity.getString(KEY_TYPE),
                            "",
                            // schoolActivity.getString(KEY_TITLE),
                            schoolActivity.getString(KEY_MESSAGE),
                            schoolActivity.getString(KEY_TIMESTAMP),
                            schoolActivity.getInt(KEY_LIKES_COUNT),
                            schoolActivity.getBoolean(IS_LIKED),
                            schoolActivity.getString(KEY_ICON)
                    );
                    if (!schoolActivity.getString(KEY_TYPE).equals(TYPE_BULLETIN))
                        sclActList.add(sclActivity);
                }
                //user.setSclActs(sclActList);
                setSclActList(sclActList);
            }
        } catch (JSONException ex) {
            Log.e("NewDataHolder", "saveDashboardDetails: ", ex);
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

    public void saveUserSections(JSONArray sectionsArray) {
        ArrayList<Sections> sectionsArrayList = new DataParser().getSectionsListFromJson(sectionsArray, false);
        //user.setSectionsList(sectionsArrayList);
        setSectionsList(sectionsArrayList);
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

    public ArrayList<TMiles> getIntroTrainingsList() {
        return introTrainingsList;
    }

    public void setIntroTrainingsList(ArrayList<TMiles> introTrainingsList) {
        this.introTrainingsList = introTrainingsList;
    }

    public ArrayList<SclActs> getSclActList() {
        return sclActList;
    }

    public void setSclActList(ArrayList<SclActs> sclActList) {
        this.sclActList = sclActList;
    }

    public SclActs getBulletin() {
        return bulletin;
    }

    public void setBulletin(SclActs bulletin) {
        this.bulletin = bulletin;
    }

    public Map<String, String> getResultsMap() {
        return resultsMap;
    }

    public void setResultsMap(Map<String, String> resultsMap) {
        this.resultsMap = resultsMap;
    }

    public ArrayList<TMiles> getArchiveList() {
        return archiveList;
    }

    public void setArchiveList(ArrayList<TMiles> archiveList) {
        this.archiveList = archiveList;
    }

    public ArrayList<DbUser> getNetworkUsers() {
        return networkUsers;
    }

    public void setNetworkUsers(ArrayList<DbUser> networkUsers) {
        this.networkUsers = networkUsers;
    }
}
