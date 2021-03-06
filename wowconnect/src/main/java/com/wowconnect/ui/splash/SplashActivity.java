package com.wowconnect.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.wowconnect.NetworkHelper;
import com.wowconnect.R;
import com.wowconnect.S2MApplication;
import com.wowconnect.SharedPreferenceHelper;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.helpers.AlertDialogListener;
import com.wowconnect.ui.helpers.DialogHelper;
import com.wowconnect.ui.landing.LandingActivity;
import com.wowconnect.ui.login.LoginActivity;

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
                    new AlertDialogListener() {
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
        if (DialogHelper.getCurrentDialog() != null)
            DialogHelper.removeAlertListener();
        super.onDestroy();
    }
}
