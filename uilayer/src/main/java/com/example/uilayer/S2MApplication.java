package com.example.uilayer;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by thoughtchimp on 12/22/2016.
 */

public class S2MApplication extends Application {
    private static Context appContext;
    @Override public void onCreate() {
        super.onCreate();
    if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        appContext=this;
    }

    public static Context getAppContext() {
        return appContext;
    }
}