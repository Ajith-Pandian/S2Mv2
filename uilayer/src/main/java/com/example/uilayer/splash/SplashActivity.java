package com.example.uilayer.splash;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.uilayer.NetworkHelper;
import com.example.uilayer.R;
import com.example.uilayer.S2MApplication;
import com.example.uilayer.SharedPreferenceHelper;
import com.example.uilayer.customUtils.Utils;
import com.example.uilayer.helpers.DialogHelper;
import com.example.uilayer.helpers.S2mAlertDialog;
import com.example.uilayer.landing.LandingActivity;
import com.example.uilayer.login.LoginActivity;
import com.google.firebase.iid.FirebaseInstanceId;

public class SplashActivity extends AppCompatActivity {
    final int TIME_IN_SECS = 2;
    NetworkHelper networkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_spash);
        //setupWindowAnimations();

        Log.d("FCM", "TOKEN: " + FirebaseInstanceId.getInstance().getToken());
       /* if (new DataBaseUtil(S2MApplication.getAppContext()).getUser() == null) {
            startNextActivity(LoginActivity.class);
        } else {
            getLoginDetails();
        }
        */
        if (SharedPreferenceHelper.getConfiguration().equals("")) {
            NetworkHelper helper = new NetworkHelper(this);
            helper.downloadConfiguration();
        }
        startNextActivity(LoginActivity.class);
    }

    void getLoginDetails() {
        if (Utils.getInstance().isNetworkConnected(this)) {
            networkHelper = new NetworkHelper(S2MApplication.getAppContext());
            networkHelper.getDashBoardDetails(new NetworkHelper.NetworkListener() {
                @Override
                public void onFinish() {
                    startNextActivity(LandingActivity.class);
                }
            });
        } else
            DialogHelper.createAlertDialog(this,
                    getString(R.string.er_no_internet),
                    getString(R.string.retry),
                    getString(R.string.cancel),
                    new S2mAlertDialog.AlertListener() {
                        @Override
                        public void onPositive() {
                            getLoginDetails();
                        }

                        @Override
                        public void onNegative() {
                            finish();
                        }
                    }
            );

    }

    void startNextActivity(Class<?> classname) {
        final Intent intent = new Intent(this, classname);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(intent);
            }
        }, TIME_IN_SECS * 1000);
    }

    @Override
    protected void onDestroy() {
        if (networkHelper != null)
            networkHelper.removeNetworkListener();
        super.onDestroy();
    }
}
