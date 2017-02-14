package com.wowconnect.ui.profile;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.wowconnect.NewDataParser;
import com.wowconnect.R;
import com.wowconnect.S2MApplication;
import com.wowconnect.domain.Constants;
import com.wowconnect.domain.network.VolleySingleton;
import com.wowconnect.models.Schools;
import com.wowconnect.ui.customUtils.Utils;
import com.wowconnect.ui.customUtils.VolleyStringRequest;
import com.wowconnect.ui.customUtils.views.PromptSpinner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.edit_text_signup_first_name)
    TextInputEditText textFirstName;
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
    @BindView(R.id.button_register)
    Button registerButton;


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
        collapsingToolbarLayout.setTitle("Register");

    }


    ArrayList<Schools> schoolsArrayList;

    void initViews() {

        //Move cursor position
        textLastName.requestFocus();
        textLastName.setSelection(textLastName.getText().toString().length());
        schoolsArrayList = new NewDataParser().getS2mConfiguration().getSchoolsArrayList();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner, R.id.text_spinner, getSchoolNames(schoolsArrayList));

        spinnerSchoolSelect.setAdapter(adapter);
        setupSchoolsSpinnerHeight();
        spinnerSchoolSelect.setOnItemSelectedListener(countrySelectedListener);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAndRegister();
                // registerUser();
            }
        });
    }

    void setupSchoolsSpinnerHeight() {
        {
            android.util.TypedValue value = new android.util.TypedValue();
            getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, value, true);
            android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float ret = value.getDimension(metrics);
            spinnerSchoolSelect.setMinimumHeight((int) (ret - 1 * metrics.density));
        }
        // Set popupWindow height

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinnerSchoolSelect);
            if (spinnerSchoolSelect.getAdapter() != null && spinnerSchoolSelect.getAdapter().getCount() > 5)
                popupWindow.setHeight(Utils.getInstance().getPixelAsDp(this, 200));

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            Log.d(TAG, "setSpinnerPopupHeight: " + e.toString());
        }
    }

    private static final String TAG = "RegisterActivity";

    void validateAndRegister() {
        if (getStringFromEditText(textFirstName).isEmpty()) {
            Utils.getInstance().showToast("First Name cannot be empty");
        } else if (getStringFromEditText(textPhone).isEmpty()) {
            Utils.getInstance().showToast("Phone Number cannot be empty");
        } else if (!getStringFromEditText(textEmail).isEmpty() && !Utils.isValidEmail(getStringFromEditText(textEmail))) {
            Utils.getInstance().showToast("Please enter valid email");
        } else if (spinnerSchoolSelect.getSelectedItem() == null) {
            Utils.getInstance().showToast("Please select a school");
        } else if (spinnerSchoolSelect.getSelectedItem().toString().equals(getString(R.string.others))) {
            if (getStringFromEditText(textSchool).isEmpty())
                Utils.getInstance().showToast("School cannot be empty");
            else
                registerUser();
        } else {
            registerUser();
        }
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
                        Utils.getInstance().showToast("Registered successfully...!!!");
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

                params.put(Constants.KEY_FIRST_NAME, getStringFromEditText(textFirstName));
                params.put(Constants.KEY_MOBILE_NO, getStringFromEditText(textPhone));
                params.put(Constants.KEY_COUNTRY_CODE, Constants.COUNTRY_CODE);

                if (!getStringFromEditText(textLastName).isEmpty())
                    params.put(Constants.KEY_EMAIL, getStringFromEditText(textEmail));
                if (!getStringFromEditText(textLastName).isEmpty())
                    params.put(Constants.KEY_LAST_NAME, getStringFromEditText(textLastName));
                if (!getStringFromEditText(textComment).isEmpty())
                    params.put(Constants.KEY_MESSAGE, getStringFromEditText(textComment));
                if (spinnerSchoolSelect.getSelectedItem() != null &&
                        !spinnerSchoolSelect.getSelectedItem().toString().equals(getString(R.string.others))) {
                    params.put(Constants.KEY_SCHOOL_ID,
                            String.valueOf(schoolsArrayList.get(spinnerSchoolSelect.getSelectedItemPosition()).getId()));
                } else {
                    params.put(Constants.KEY_SCHOOL_NAME, getStringFromEditText(textSchool));
                }

                Log.d("getParams", "getParams: " + params.toString());
                return params;
            }
        };


        VolleySingleton.getInstance(S2MApplication.getAppContext()).addToRequestQueue(registerUser);
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
