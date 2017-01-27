package com.example.uilayer.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;


import com.example.domainlayer.temp.DataHolder;
import com.example.uilayer.NewDataHolder;
import com.example.uilayer.R;
import com.example.uilayer.signup.SignUpActivity;
import com.example.uilayer.landing.LandingActivity;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, OtpFragment.OtpListener {


    boolean isOTP = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOTP)
            replaceFragment(OtpFragment.newInstance());
        else
            replaceFragment(LoginFragment.newInstance());
    }

    @Override
    public void onEnteredNumber(final String stringResponse) {
        gotoOtp();
    }

    void gotoOtp() {
        replaceFragment(OtpFragment.newInstance());
        isOTP = true;
    }

    void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void launchSignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class).putExtra("isSignUp",false));
    }

    void launchLanding() {
        startActivity(new Intent(LoginActivity.this, LandingActivity.class));
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isOTP", isOTP);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isOTP = savedInstanceState.getBoolean("isOTP");
    }


    @Override
    public void onOtpEntered() {
       /* if (NewDataHolder.getInstance(this).getUser().getLastLogin().equals("null"))
            launchSignUp();
        else*/
            launchLanding();
        finish();
    }
}
