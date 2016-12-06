package com.example.domainlayer.database;

import android.content.Context;
import android.util.Log;

import com.example.domainlayer.Constants;
import com.example.domainlayer.models.Sections;
import com.example.domainlayer.models.User;
import com.j256.ormlite.dao.Dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.List;

import static com.example.domainlayer.Constants.KEY_CLASS;
import static com.example.domainlayer.Constants.KEY_COMPLETED_MILESTONES;
import static com.example.domainlayer.Constants.KEY_ID;
import static com.example.domainlayer.Constants.KEY_SECTION;
import static com.example.domainlayer.Constants.KEY_TOTAL_MILESTONES;
import static com.example.domainlayer.Constants.TYPE_BULLETIN;

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

    private Dao<User, Integer> getLocalUserDao() {
        try {
            if (helper == null)
                helper = new DataBaseHelper(context);
            return helper.getUserDao();
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getLocalUserDao: ", ex);
            throw new RuntimeException("Cannot get user Dao");
        }
    }

    public User getUser() {
        try {
            Dao<User, Integer> userDao = getLocalUserDao();
            final List<User> userList = userDao.queryForAll();
            if (userList.size() > 0)
                return userList.get(0);
            else
                throw new RuntimeException("No users in table");

        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getUser: ", ex);
            throw new RuntimeException("No users in table");
        }

    }

    public void setUser(JSONObject userJsonObject) {
        try {
            Dao<User, Integer> userDao = getLocalUserDao();
            userDao.createOrUpdate(getUserFromJson(userJsonObject));
            Log.d(context.getClass().getSimpleName(), "setUser:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getUser: ", ex);
            throw new RuntimeException("Cannot create user");
        }
    }

    private User getUserFromJson(JSONObject userJson) {
        User user;
        try {
            user = new User();
            //Profile data
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
            //SclActivities Data


            return user;
        } catch (JSONException ex) {
            Log.e(TAG, "getUserFromJson: ", ex);
            throw new RuntimeException("Json exception");
        }

    }
}
