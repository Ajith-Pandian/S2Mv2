package com.example.uilayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

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
    Spinner spinnerSchoolSelect;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_signup_camera)
    FloatingActionButton cameraButton;
    @BindView(R.id.layout_custom_school)
    LinearLayout customSchoolLayout;
    @BindView(R.id.button_register)
    Button buttonRegister;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        loadData();

    }

    void loadData() {
        DataHolder holder=DataHolder.getInstance(getApplicationContext());
        textFirstName.setText(holder.getFirstName());
        textLastName.setText(holder.getLastName());
        textEmail.setText(holder.getEmail());
        textPhone.setText(holder.getPhoneNum());
        textComment.setText("");
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
                startActivity(new Intent(SignUpActivity.this, SchoolDetailActivity.class));
            }
        });
    }

}
