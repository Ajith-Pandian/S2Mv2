package com.example.uilayer;

import android.content.Context;

/**
 * Created by thoughtchimp on 11/29/2016.
 */

public class DataHolder {
    private static DataHolder mInstance;
    private String otp;

    public static synchronized DataHolder getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataHolder();
        }
        return mInstance;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
