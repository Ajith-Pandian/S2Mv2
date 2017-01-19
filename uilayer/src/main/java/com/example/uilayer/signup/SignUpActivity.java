package com.example.uilayer.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.PromptSpinner;
import com.example.uilayer.landing.LandingActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.edit_text_signup_first_name)
    EditText textFirstName;
    @BindView(R.id.edit_text_signup_last_name)
    EditText textLastName;
    @BindView(R.id.edit_text_signup_email)
    EditText textEmail;
    @BindView(R.id.edit_text_signup_phone)
    EditText textPhone;
    @BindView(R.id.edit_text_signup_school)
    EditText textSchool;
    @BindView(R.id.edit_text_signup_comment)
    EditText textComment;
    @BindView(R.id.spinner_school_select)
    PromptSpinner spinnerSchoolSelect;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_signup_camera)
    FloatingActionButton cameraButton;
    @BindView(R.id.layout_custom_school)
    LinearLayout customSchoolLayout;
    @BindView(R.id.button_register)
    Button buttonRegister;
    @BindView(R.id.user_image)
    ImageView imageUser;

    @BindView(R.id.collapse_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    AdapterView.OnItemSelectedListener countrySelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> spinner, View container,
                                   int position, long id) {
            if (spinnerSchoolSelect.getSelectedItem().toString().equals(getString(R.string.others)))
                customSchoolLayout.setVisibility(View.VISIBLE);
            else
                customSchoolLayout.setVisibility(View.GONE);

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }
    };
    boolean isSignUp;//true if signup -- false for modification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        if (getIntent() != null)
            isSignUp = getIntent().getBooleanExtra("isSignUp", false);

        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        configureTiltle();
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(SignUpActivity.this,LandingActivity.class));
            }
        });
        if(!isSignUp)
        {loadData();}
    }

    void configureTiltle() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Register");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
                collapsingToolbarLayout.setTitleEnabled(isShow);
            }
        });
    }

    void loadData() {
        DataHolder holder = DataHolder.getInstance(getApplicationContext());
        textFirstName.setText(holder.getFirstName());
        textLastName.setText(holder.getLastName());
        textEmail.setText(holder.getEmail());
        textPhone.setText(holder.getPhoneNum());
        textComment.setText("");
        Picasso.with(getApplicationContext())
                .load(holder.getAvatar())
                .placeholder(R.drawable.ph_user_big)
                .into(imageUser);
    }

    void initViews() {

        //Move cursor position
        textLastName.requestFocus();
        textLastName.setSelection(textLastName.getText().toString().length());
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.school_names, R.layout.spinner_text);
        spinnerSchoolSelect.setAdapter(adapter);
        spinnerSchoolSelect.setOnItemSelectedListener(countrySelectedListener);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SignUpActivity.this, LandingActivity.class));
                Snackbar.make(view, "Registered Successfully..!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                updateUser();
            }
        });
    }

    void updateUser()
    {

    }

}
