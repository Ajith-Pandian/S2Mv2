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
import java.util.ArrayList;
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
            //throw new RuntimeException("Cannot get user Dao");
            return null;
        }
    }

    public DbUser getUser() {
        try {
            Dao<DbUser, Integer> userDao = getLocalUserDao();
            final List<DbUser> userList = userDao.queryForAll();
            if (userList.size() > 0)
                return userList.get(0);
            else {
                return null;
                // throw new RuntimeException("No users in table");
            }

        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getUser: ", ex);
            throw new RuntimeException("No users in table");
        }

    }

    public void setUser(DbUser user) {
        Dao<DbUser, Integer> userDao = getLocalUserDao();
        if (userDao != null)
            try {
                //  userDao.createOrUpdate(getUserFromJson(userJsonObject));
                userDao.createOrUpdate(user);
                Log.d(context.getClass().getSimpleName(), "setUser:");
            } catch (SQLException ex) {
                Log.e(context.getClass().getSimpleName(), "getUser: ", ex);
                throw new RuntimeException("Cannot create user");
            }
    }

    public void setNetworkUsers(ArrayList<DbUser> usersList) {

        try {
            for (DbUser user : usersList) {
                Dao<DbUser, Integer> userDao = getLocalUserDao();
                if (userDao != null) {
                    userDao.createOrUpdate(user);
                }
            }
            Log.d(context.getClass().getSimpleName(), "setNetworkUsers:");
        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "setNetworkUsers: ", ex);
            throw new RuntimeException("Cannot create user");
        }
    }


    public ArrayList<DbUser> getNetwokUsers() {
        try {
            Dao<DbUser, Integer> userDao = getLocalUserDao();
            if (userDao != null) {
                final List<DbUser> userList = userDao.queryForAll();
                if (userList.size() > 0)
                    return new ArrayList<>(userList);
                else {
                    return null;
                }
            } else throw new RuntimeException("No users in table");

        } catch (SQLException ex) {
            Log.e(context.getClass().getSimpleName(), "getNetwokUsers: ", ex);
            throw new RuntimeException("No users in table");
        }


    }
}