package com.wowconnect;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.database.DataBaseUtil;
import com.wowconnect.domain.temp.DataParser;
import com.wowconnect.models.DbUser;
import com.wowconnect.models.Schools;
import com.wowconnect.models.SclActs;
import com.wowconnect.models.Sections;
import com.wowconnect.models.mcq.MCQs;
import com.wowconnect.models.milestones.TMileData;
import com.wowconnect.models.milestones.TMiles;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static com.wowconnect.domain.Constants.IS_LIKED;
import static com.wowconnect.domain.Constants.KEY_ACTIVITIES;
import static com.wowconnect.domain.Constants.KEY_BULLETIN_BOARD;
import static com.wowconnect.domain.Constants.KEY_ICON;
import static com.wowconnect.domain.Constants.KEY_ID;
import static com.wowconnect.domain.Constants.KEY_LIKES_COUNT;
import static com.wowconnect.domain.Constants.KEY_MESSAGE;
import static com.wowconnect.domain.Constants.KEY_OPTIONS;
import static com.wowconnect.domain.Constants.KEY_SCHOOLS;
import static com.wowconnect.domain.Constants.KEY_TIMESTAMP;
import static com.wowconnect.domain.Constants.KEY_TITLE;
import static com.wowconnect.domain.Constants.KEY_TYPE;
import static com.wowconnect.domain.Constants.ROLE_TEACHER;
import static com.wowconnect.domain.Constants.TYPE_BULLETIN;


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
    private ArrayList<MCQs> currentMileMcqs;
    private ArrayList<Sections> sectionsList;
    private ArrayList<DbUser> networkUsers;
    private ArrayList<SclActs> sclActList;
    private SclActs bulletin;
    private String currentMileTitle;
    private Map<String, String> resultsMap;

    private NewDataHolder(Context context) {
        this.context = context.getApplicationContext();
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
            user.setId(userJson.getInt(KEY_ID));
            user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
            if (!userJson.isNull(Constants.KEY_LAST_NAME))
                user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
            user.setPhoneNum(userJson.getString(Constants.KEY_MOBILE_NO));
            user.setEmail(userJson.getString(Constants.KEY_EMAIL));
            user.setAccessToken(userJson.getString(Constants.KEY_ACCESS_TOKEN));
            user.setFirstLogin(userJson.getBoolean(Constants.KEY_IS_FIRST_LOGIN));
            //user.setFirstLogin(true);

            String userType = userJson.getString(KEY_TYPE);

            user.setAvatar(userJson.getString(Constants.KEY_PROFILE_PICTURE));

            if (!userJson.isNull(KEY_OPTIONS)) {
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
            for (int i = 0; i < schoolsJsonArray.length(); i++) {
                Schools school = new Schools();
                JSONObject schoolJsonObj = schoolsJsonArray.getJSONObject(i);
                school.setId(schoolJsonObj.getInt(KEY_ID));
                school.setName(schoolJsonObj.getString(Constants.KEY_NAME));
                school.setLogo(schoolJsonObj.getString(Constants.KEY_LOGO));
                school.setLocality(schoolJsonObj.getString(Constants.KEY_LOCALITY));
                school.setCity(schoolJsonObj.getString(Constants.KEY_CITY));
                String userRoles = schoolJsonObj.getString(Constants.KEY_ROLES);
                Log.d("USER roles", "setLoginResult: " + userRoles);
                user.setRoles(userRoles);

                if (i == 0)
                    SharedPreferenceHelper.setUserRoles(getStringsFromArray(schoolJsonObj.getJSONArray(Constants.KEY_ROLES)));
                schoolsArrayList.add(school);
            }
            setSchools(schoolsArrayList);
            user.setType(userType);
            user.setSchoolsList(schoolsArrayList);
            if (schoolsArrayList.size() > 0) {
                user.setSchoolId(schoolsArrayList.get(0).getId());
                user.setSchoolName(schoolsArrayList.get(0).getName());
                SharedPreferenceHelper.setSchoolId(user.getSchoolsList().get(0).getId());
            }

          /* */
            // setUser(user);

            SharedPreferenceHelper.setAccessToken(user.getAccessToken());
            SharedPreferenceHelper.setUserId(user.getId());

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
        return new DataBaseUtil(context).getUser(SharedPreferenceHelper.getUserId());
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


    public ArrayList<Sections> getSectionsList() {
        return new DataBaseUtil(context).getUserSections();
    }

    public void setSectionsList(ArrayList<Sections> sectionsList) {
        new DataBaseUtil(context).setUserSections(sectionsList);
    }

    public ArrayList<TMiles> getIntroTrainingsList() {
        return introTrainingsList;
    }

    public void setIntroTrainingsList(ArrayList<TMiles> introTrainingsList) {
        this.introTrainingsList = introTrainingsList;
    }

    private ArrayList<SclActs> sclActsArrayList;

    public ArrayList<SclActs> getSclActList() {
        return sclActsArrayList != null ? sclActsArrayList : new DataBaseUtil(context).getSchoolActivities();
    }

    public void setSclActList(ArrayList<SclActs> sclActList) {
        this.sclActsArrayList = sclActList;
        new DataBaseUtil(context).setSchoolActivities(sclActsArrayList);
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
        return this.networkUsers;
    }

    public void setNetworkUsers(ArrayList<DbUser> networkUsers) {
        new DataBaseUtil(context).setNetworkUsers(networkUsers);
        this.networkUsers = networkUsers;
    }

    public Schools getSchoolById(int SchoolId) {
        return new DataBaseUtil(context).getSchoolById(SchoolId);
    }

    public void setSchools(ArrayList<Schools> schoolsArrayListsers) {
        new DataBaseUtil(context).setSchools(schoolsArrayListsers);
    }

    public ArrayList<DbUser> getTeachersList() {
        ArrayList<DbUser> teachersList = new ArrayList<>();
        ArrayList<DbUser> usersList = new DataBaseUtil(context).getNetworkUsers();
        for (int i = 0; i < usersList.size(); i++) {
            if (new NewDataParser().getUserRoles(context, usersList.get(i).getId()).contains(ROLE_TEACHER))
                teachersList.add(usersList.get(i));
        }
        return teachersList;
    }

    public ArrayList<MCQs> getCurrentMileMcqs() {
        return currentMileMcqs;
    }

    public void setCurrentMileMcqs(ArrayList<MCQs> currentMileMcqs) {
        this.currentMileMcqs = currentMileMcqs;
    }


}
