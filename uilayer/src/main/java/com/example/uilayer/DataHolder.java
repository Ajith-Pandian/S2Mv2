package com.example.uilayer;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class DataHolder {
    private static DataHolder mInstance;
    private String otp;
    private String firstName;
    private String lastName;
    private String phoneNum;
    private String accessToken;
    private String lastLogin;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
    private JSONObject loginResultJson;

    public static synchronized DataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHolder();
        }
        return mInstance;
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
}
