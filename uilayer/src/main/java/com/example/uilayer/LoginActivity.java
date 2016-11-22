package com.example.uilayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, OtpFragment.OtpListener {


    boolean isOTP=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
    public void onEnteredNumber() {
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

    void lauchSignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
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
        lauchSignUp();
        Toast.makeText(getApplicationContext(), "Otp entered", Toast.LENGTH_SHORT).show();
    }
}
