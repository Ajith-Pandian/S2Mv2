package com.example.uilayer.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.domainlayer.Constants;
import com.example.domainlayer.network.VolleySingleton;
import com.example.uilayer.customUtils.VolleyStringRequest;
import com.example.uilayer.DataHolder;
import com.example.uilayer.R;
import com.example.uilayer.customUtils.views.PromptSpinner;
import com.example.uilayer.landing.LandingActivity;
import com.example.uilayer.manage.ManageTeachersActivity;
import com.squareup.picasso.Picasso;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.domainlayer.Constants.KEY_ACCESS_TOKEN;
import static com.example.domainlayer.Constants.KEY_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TEMP_DEVICE_TYPE;
import static com.example.domainlayer.Constants.TYPE_TEACHER;

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
    VolleyStringRequest updateRequest;
    String title;
    DataHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        if (getIntent() != null)
            isSignUp = getIntent().getBooleanExtra("isSignUp", false);

        initViews();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        configureTitle();
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // startActivity(new Intent(SignUpActivity.this,LandingActivity.class));
            }
        });
        if (!isSignUp) {
            loadData();
            schoolSpinnerLayout.setVisibility(View.GONE);
            commentLayout.setVisibility(View.GONE);
            nextButtonLayout.setVisibility(View.VISIBLE);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchLanding();
                }
            });
            customSchoolLayout.setVisibility(View.GONE);
            title = "Update";
            registerButton.setText(title);
        }
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
        holder = DataHolder.getInstance(getApplicationContext());
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
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SignUpActivity.this, LandingActivity.class));

                updateUser();
            }
        });
    }

    void updateUser() {
        updateRequest = new VolleyStringRequest(Request.Method.POST, Constants.USER_DETAILS_URL
                + String.valueOf(DataHolder.getInstance(this).getUserId()) + "/update"
                ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                        Log.d("updateRequest", "onResponse: " + response);
                        String suffix = "";
                        suffix = title.substring(title.length() - 1).equals("e") ? "d" : "ed";
                        Snackbar.make(registerButton,
                                title + suffix + " Successfully..!!",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();


                        if (DataHolder.getInstance(SignUpActivity.this).getUserType().equals(TYPE_TEACHER))
                            launchLanding();
                        else
                            launchManageTeachersAndSections();
                        finish();

                    }
                },
                new VolleyStringRequest.VolleyErrListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        Log.d("updateRequest-error", "onResponse: " + error);
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new ArrayMap<>();
                header.put(KEY_ACCESS_TOKEN, String.valueOf(DataHolder.getInstance(SignUpActivity.this).getAccessToken()));
                header.put(KEY_DEVICE_TYPE, TEMP_DEVICE_TYPE);
                return header;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new ArrayMap<>();

                params.put(Constants.KEY_EMAIL, getStringFromEditText(textEmail));
                params.put(Constants.KEY_PHONE_NUM, getStringFromEditText(textPhone));
                params.put(Constants.KEY_FIRST_NAME, getStringFromEditText(textFirstName));
                params.put(Constants.KEY_LAST_NAME, getStringFromEditText(textLastName));
                params.put(Constants.KEY_AVATAR, "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fe/Mark_Zuckerberg_em_setembro_de_2014.jpg/220px-Mark_Zuckerberg_em_setembro_de_2014.jpg");
                return params;
            }
        };


        VolleySingleton.getInstance(this).addToRequestQueue(updateRequest);
    }


    String getStringFromEditText(TextView editText) {
        return editText.getText().toString();
    }

    void launchLanding() {
        startActivity(new Intent(SignUpActivity.this, LandingActivity.class));
    }

    void launchManageTeachersAndSections() {
        startActivity(new Intent(SignUpActivity.this,
                ManageTeachersActivity.class)
                .putExtra("isTeachers", true)
                .putExtra("isFirstTime", true));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateRequest != null) {
            updateRequest.removeStatusListener();
        }
    }
}
