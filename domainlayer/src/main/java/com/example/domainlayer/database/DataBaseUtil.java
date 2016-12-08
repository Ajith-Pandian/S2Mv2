package com.example.domainlayer.database;

import android.content.Context;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.SclActs;
import com.example.domainlayer.models.Sections;
import com.j256.ormlite.dao.Dao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;



/**
 * Created by thoughtchimp on 12/6/2016.
 */

public class DataBaseUtil {
    final String TAG = "DataBaseUtil";
    private Context context;
    private DataBaseHelper helper;

    public DataBaseUtil(Context context) {
        this.context = context;
    }

    private Dao<DbUser, Integer> getLocalUserDao() {
        try {
            if (helper == null)
                helper = new DataBaseHelper(context);
            return helper.getUserDao();
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getLocalUserDao: ", ex);
            throw new RuntimeException("Cannot get user Dao");
        }
    }

    public DbUser getUser() {
        try {
            Dao<DbUser, Integer> userDao = getLocalUserDao();
            final List<DbUser> userList = userDao.queryForAll();
            if (userList.size() > 0)
                return userList.get(0);
            else
                throw new RuntimeException("No users in table");

        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getUser: ", ex);
            throw new RuntimeException("No users in table");
        }

    }

    public void setUser(DbUser  user) {
        try {
            Dao<DbUser, Integer> userDao = getLocalUserDao();
          //  userDao.createOrUpdate(getUserFromJson(userJsonObject));
            userDao.createOrUpdate(user);
            Log.d(context.getClass().getSimpleName(), "setUser:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getUser: ", ex);
            throw new RuntimeException("Cannot create user");
        }
    }

/*    private User getUserFromJson(JSONObject userJson) {
      *//*  User user;
        try {
            user = new User();
           *//**//* //Profile data
            user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
            user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
            user.setEmail(userJson.getString(Constants.KEY_EMAIL));
            user.setPhoneNum(userJson.getString(Constants.KEY_PHONE_NUM));
            user.setMiles(userJson.getString(Constants.KEY_MILES));
            user.setWow(userJson.getString(Constants.KEY_WOW));
            //Section Data
            Sections section = new Sections();
            JSONObject bulletinJson = userJson.getJSONObject(TYPE_BULLETIN);
            section.setId(bulletinJson.getInt(KEY_ID));
            section.set_Class(bulletinJson.getString(KEY_CLASS));
            section.setSection(bulletinJson.getString(KEY_SECTION));
            section.setCompletedMiles(bulletinJson.getInt(KEY_COMPLETED_MILESTONES));
            section.setTotalMiles(bulletinJson.getInt(KEY_TOTAL_MILESTONES));
            user.setSections(section);
            //SclActivities Data*//**//*

        *//**//*    user.setFirstName(userJson.getString(Constants.KEY_FIRST_NAME));
            user.setLastName(userJson.getString(Constants.KEY_LAST_NAME));
            user.setEmail(userJson.getString(Constants.KEY_EMAIL));
            user.setPhoneNum(userJson.getString(Constants.KEY_PHONE_NUM));
            user.setLastLogin(userJson.getString(Constants.KEY_LAST_LOGIN));
            user.setSchoolId(userJson.getInt(Constants.KEY_SCHOOL_ID));


            JSONObject bulletinJson = userJson.getJSONObject(TYPE_BULLETIN);
            SclActs bulletin = new SclActs();
            bulletin.setId(bulletinJson.getInt(KEY_ID));
            bulletin.setUserId(bulletinJson.getInt(KEY_USER_ID));
            bulletin.setMsg(bulletinJson.getString(KEY_MESSAGE));
            bulletin.setType(bulletinJson.getString(KEY_TYPE));
            bulletin.setTimeStamp(bulletinJson.getString(KEY_TIMESTAMP));
            user.setBulletin(bulletin);

            JSONArray sectionsArray = userJson.getJSONArray(KEY_SECTIONS);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject sectionObject = sectionsArray.getJSONObject(i);
                Sections section
                        = new Sections(sectionObject.getInt(KEY_ID),
                        sectionObject.getString(KEY_CLASS),
                        sectionObject.getString(KEY_SECTION),
                        sectionObject.getInt(KEY_COMPLETED_MILESTONES),
                        sectionObject.getInt(KEY_TOTAL_MILESTONES));
                userJson.add(section);
            }
            user.setSectionsList(sectionsList);

            JSONArray schoolActivities = userJson.getJSONArray(KEY_ACTIVITIES);
            for (int i = 0; i < sectionsArray.length(); i++) {
                JSONObject schoolActivity = schoolActivities.getJSONObject(i);
                SclActs sclActivities
                        = new SclActs(schoolActivity.getInt(KEY_ID),
                        schoolActivity.getInt(KEY_USER_ID),
                        schoolActivity.getString(KEY_MESSAGE),
                        schoolActivity.getString(KEY_TYPE),
                        schoolActivity.getString(KEY_TIMESTAMP));
                sclActList.add(sclActivities);
            }
            user.setSclActs(sclActList);*//**//*

            return user;
        } catch (JSONException ex) {
            Log.e(TAG, "getUserFromJson: ", ex);
            throw new RuntimeException("Json exception");
        }
*//*
    }*/
}
