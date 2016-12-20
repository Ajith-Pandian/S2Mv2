package com.example.uilayer.splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;

import com.example.domainlayer.database.DataBaseUtil;
import com.example.uilayer.NetworkHelper;
import com.example.uilayer.R;
import com.example.uilayer.landing.LandingActivity;
import com.example.uilayer.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        setupWindowAnimations();
        if (new DataBaseUtil(getApplicationContext()).getUser() == null)
            startNextActivity(LoginActivity.class);
        else {
            new NetworkHelper(getApplicationContext()).getUserDetails(new NetworkHelper.NetworkListener() {
                @Override
                public void onFinish() {
                    startNextActivity(LandingActivity.class);
                }
            });
        }

    }

    void startNextActivity(Class<?> classname) {
        Intent intent = new Intent(this, classname);
        finish();
        startActivity(intent);
    }
    @SuppressWarnings("NewApi")
    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }
}
