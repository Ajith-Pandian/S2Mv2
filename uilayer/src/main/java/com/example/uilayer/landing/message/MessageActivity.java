package com.example.uilayer.landing.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.uilayer.R;

import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
    }
}
