package com.example.uilayer;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.domainlayer.Constants;

/**
 * Created by thoughtchimp on 1/19/2017.
 */

public class SharedPreferenceHelper {
    private final static String PREF_FILE = "PREF";

    /**
     * Set a string shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Set a integer shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getSchoolId() {
        return getSharedPreferenceInt(S2MApplication.getAppContext(), Constants.KEY_SCHOOL_ID, -1);
    }

    public static void setSchoolId(int schoolId) {
        setSharedPreferenceInt(S2MApplication.getAppContext(), Constants.KEY_SCHOOL_ID, schoolId);
    }

    public static int getUserId() {
        return getSharedPreferenceInt(S2MApplication.getAppContext(), Constants.KEY_USER_ID, -1);
    }

    public static void setUserId(int userId) {
        setSharedPreferenceInt(S2MApplication.getAppContext(), Constants.KEY_USER_ID, userId);
    }

    public static String getAccessToken() {
        return getSharedPreferenceString(S2MApplication.getAppContext(), Constants.KEY_ACCESS_TOKEN, "");
    }

    public static void setAccessToken(String accessToken) {
        setSharedPreferenceString(S2MApplication.getAppContext(), Constants.KEY_ACCESS_TOKEN, accessToken);
    }

    /**
     * Set a Boolean shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Get a string shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static String getSharedPreferenceString(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, defValue);
    }

    /**
     * Get a integer shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static int getSharedPreferenceInt(Context context, String key, int defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getInt(key, defValue);
    }

    /**
     * Get a boolean shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static boolean getSharedPreferenceBoolean(Context context, String key, boolean defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getBoolean(key, defValue);
    }
}