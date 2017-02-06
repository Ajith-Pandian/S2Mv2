package com.example.uilayer.profile;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.database.DataBaseUtil;
import com.example.domainlayer.models.DbUser;
import com.example.domainlayer.models.Schools;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.NewDataParser;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.views.PromptSpinner;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
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
    @BindView(R.id.layout_custom_school)
    LinearLayout customSchoolLayout;
    @BindView(R.id.layout_school_spinner)
    LinearLayout schoolSpinnerLayout;
    @BindView(R.id.layout_comment)
    LinearLayout commentLayout;
    @BindView(R.id.layout_next_button)
    LinearLayout nextButtonLayout;
    @BindView(R.id.button_register)
    Button registerButton;
    @BindView(R.id.button_next)
    Button nextButton;

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
    VolleyStringRequest registerUser;
    String title;
    DbUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        initViews();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        configureTitle();

        /*if (!isSignUp) {
            loadData();
            schoolSpinnerLayout.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);
            nextButtonLayout.setVisibility(View.VISIBLE);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchLanding();
                    finish();
                }
            });
            customSchoolLayout.setVisibility(View.GONE);
            title = "Update";
            registerButton.setText(title);
        }*/
    }

    void configureTitle() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(title);
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
        user = new DataBaseUtil(getApplicationContext()).getUser();
        textFirstName.setText(user.getFirstName());
        textLastName.setText(user.getLastName());
        textEmail.setText(user.getEmail());
        textPhone.setText(user.getPhoneNum());
        textComment.setText("");
    }

    ArrayList<Schools> schoolsArrayList;

    void initViews() {

        //Move cursor position
        textLastName.requestFocus();
        textLastName.setSelection(textLastName.getText().toString().length());
        schoolsArrayList = new NewDataParser().getS2mConfiguration().getSchoolsArrayList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_text, getSchoolNames(schoolsArrayList));

        spinnerSchoolSelect.setAdapter(adapter);
        spinnerSchoolSelect.setOnItemSelectedListener(countrySelectedListener);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }


    ArrayList<String> getSchoolNames(ArrayList<Schools> schoolsArrayList) {
        ArrayList<String> schoolNames = new ArrayList<>();
        for (int i = 0; i < schoolsArrayList.size(); i++)
            schoolNames.add(schoolsArrayList.get(i).getName());
        schoolNames.add(getString(R.string.others));
        return schoolNames;
    }

    void registerUser() {
        registerUser = new VolleyStringRequest(Request.Method.POST, Constants.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("registerUser", "onResponse: " + response);
                        Snackbar.make(registerButton,
                                "Registered Successfully..!!",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        finish();
                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("registerUser", "onErrorResponse: " + error);
                    }
                }, new VolleyStringRequest.StatusCodeListener() {
            String TAG = "VolleyStringReq";

            @Override
            public void onBadRequest() {
                Log.d(TAG, "onBadRequest: ");
            }

            @Override
            public void onUnauthorized() {
                Log.d(TAG, "onUnauthorized: ");
            }

            @Override
            public void onNotFound() {
                Log.d(TAG, "onNotFound: ");
            }

            @Override
            public void onConflict() {
                Log.d(TAG, "onConflict: ");
            }

            @Override
            public void onTimeout() {
                Log.d(TAG, "onTimeout: ");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();

                params.put(Constants.KEY_EMAIL, getStringFromEditText(textEmail));
                params.put(Constants.KEY_MOBILE_NO, getStringFromEditText(textPhone));
                params.put(Constants.KEY_FIRST_NAME, getStringFromEditText(textFirstName));
                params.put(Constants.KEY_LAST_NAME, getStringFromEditText(textLastName));
                params.put(Constants.KEY_MESSAGE, getStringFromEditText(textComment));
                params.put(Constants.KEY_COUNTRY_CODE, Constants.COUNTRY_CODE);
                if (spinnerSchoolSelect.getSelectedItem() != null) {
                    params.put(Constants.KEY_SCHOOL_ID,
                            String.valueOf(schoolsArrayList.get(spinnerSchoolSelect.getSelectedItemPosition()).getId()));
                } else {
                    params.put(Constants.KEY_SCHOOL_NAME, getStringFromEditText(textSchool));
                }

                Log.d("getParams", "getParams: " + params.toString());
                return params;
            }
        };


        VolleySingleton.getInstance(this).addToRequestQueue(registerUser);
    }


    String getStringFromEditText(TextView editText) {
        return editText.getText().toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerUser != null) {
            registerUser.removeStatusListener();
        }
    }

   /* void handleError(String error) {
        try {
            JSONObject errorJson = new JSONObject(error);
            String message = errorJson.getString(KEY_MESSAGE);
            JSONObject errorReasons = errorJson.getString("errors");
        } catch (Exception e) {
        }
    }*/
}
